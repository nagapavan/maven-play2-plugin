/*
 * Copyright 2013 OW2 Nanoko Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nanoko.playframework.mojo;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.MojoExecutionException;

import static java.lang.System.*;

/**
 * Launch the Play application in debug mode (play debug run)
 *
 * @goal debug
 * @requiresProject false
 * @requiresDependencyResolution provided
 */
public class Play2DebugMojo extends AbstractPlay2Mojo {

    public void execute() throws MojoExecutionException {

        String line = getPlay2().getAbsolutePath();

        CommandLine cmdLine = CommandLine.parse(line);

        cmdLine.addArgument("debug");
        cmdLine.addArguments(getPlay2SystemPropertiesArguments(), false);
        cmdLine.addArgument("run");
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(err, out, in));

        // As where not linked to a project, we can't set the working directory.
        // So it will use the directory where mvn was launched.

        executor.setExitValue(0);
        try {
            executor.execute(cmdLine, getEnvironment());
        } catch (IOException e) {
            // Ignore.
        }
    }
}
