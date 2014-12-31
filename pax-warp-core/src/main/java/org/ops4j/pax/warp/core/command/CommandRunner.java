/*
 * Copyright 2014 EOS UPTRADE GmbH.
 *
 * This is proprietary software. All rights reserved. Unauthorized use is prohibited.
 */
package org.ops4j.pax.warp.core.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author hwellmann
 *
 */
public interface CommandRunner {

    void dump(String jdbcUrl, String username, String password, OutputStream os);

    void dump(DataSource ds, OutputStream os);

    void dump(Connection dbc, OutputStream os);

    void dumpData(String jdbcUrl, String username, String password, OutputStream os);

    void dumpData(Connection dbc, OutputStream os);

    void dumpDataOnly(String jdbcUrl, String username, String password, OutputStream os);

    void dumpDataOnly(Connection dbc, OutputStream os);

    void migrate(String jdbcUrl, String username, String password, InputStream is);

    void migrate(DataSource ds, InputStream is, String dbms);

    void migrate(Connection dbc, InputStream is, String dbms);

    void insertData(Connection dbc, InputStream is, String dbms);
    void insertData(Connection dbc, InputStream is, String dbms, List<String> excludedTables);
}
