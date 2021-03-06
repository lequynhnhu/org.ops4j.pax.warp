/*
 * Copyright 2014 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.warp.core.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.warp.core.changelog.ChangeLogWriter;
import org.ops4j.pax.warp.exc.WarpException;
import org.ops4j.pax.warp.jaxb.gen.ChangeLog;
import org.ops4j.pax.warp.jaxb.gen.ChangeSet;
import org.ops4j.pax.warp.jaxb.gen.CreateTable;

@RunWith(PaxExam.class)
public class DatabaseModelBuilderTest {

    @Inject
    private ChangeLogWriter changeLogWriter;

    @Test
    public void shouldGenerateChangeLogH2() throws SQLException, JAXBException, IOException {
        shouldGenerateChangeLog("jdbc:h2:mem:test", "h2.sql");
    }

    @Test
    public void shouldNotTreatUnderscoreAsWildcard() throws SQLException, JAXBException, IOException {
        DatabaseModel database = buildDatabaseModel("jdbc:h2:mem:test", "user_.sql");
        CreateTable table = database.getTable("USER_");
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumn().size(), is(2));
    }

    @Test
    public void shouldGenerateChangeLogDerby() throws SQLException, JAXBException, IOException {
        shouldGenerateChangeLog("jdbc:derby:memory:test;create=true", "derby.sql");
    }

    private void shouldGenerateChangeLog(String jdbcUrl, String scriptName) throws SQLException, JAXBException, IOException {
        DatabaseModel database = buildDatabaseModel(jdbcUrl, scriptName);
        ChangeLog changeLog = new ChangeLog();
        changeLog.setVersion("0.1");
        changeLog.getChangeSet();

        ChangeSet changeSet = new ChangeSet();
        changeSet.setId("1");
        List<Object> changes = changeSet.getChanges();
        changes.addAll(database.getTables());
        changes.addAll(database.getPrimaryKeys());
        changes.addAll(database.getForeignKeys());
        changeLog.getChangeSet().add(changeSet);

        OutputStreamWriter writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        changeLogWriter.write(changeLog, writer);
    }

    private DatabaseModel buildDatabaseModel(String jdbcUrl, String scriptName) throws SQLException, IOException {
        Connection dbc = DriverManager.getConnection(jdbcUrl, null, null);
        Files.lines(Paths.get("src/test/resources/sql", scriptName)).forEach(s -> runUpdate(dbc, s));

        DatabaseModelBuilder inspector = new DatabaseModelBuilder(dbc);
        DatabaseModel database = inspector.buildDatabaseModel();
        assertThat(database, is(notNullValue()));
        return database;

    }

    private void runUpdate(Connection dbc, String sql) {
        try (Statement st = dbc.createStatement()) {
            st.executeUpdate(sql);
        }
        catch (SQLException exc) {
            throw new WarpException(exc);
        }
    }
}
