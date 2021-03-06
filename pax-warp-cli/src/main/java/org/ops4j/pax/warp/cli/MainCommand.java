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
package org.ops4j.pax.warp.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Main command, encapsulating command-independent options.
 *
 * @author Harald Wellmann
 *
 */
@Parameters
public class MainCommand {

    @Parameter(names = "--version", description = "prints version")
    private Boolean version;

    @Parameter(names = "--help", description = "prints help", help = true)
    private Boolean help;


    /**
     * @return the version
     */
    public Boolean isVersion() {
        return version;
    }


    /**
     * @return the help
     */
    public Boolean isHelp() {
        return help;
    }
}
