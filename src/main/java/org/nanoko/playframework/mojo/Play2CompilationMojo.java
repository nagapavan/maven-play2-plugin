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

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;

/**
 * Compile the Play application
 *
 * @goal compile
 * @phase compile
 */
public class Play2CompilationMojo
        extends AbstractPlay2Mojo {


    public void execute()
            throws MojoExecutionException {

        String line = getPlay2().getAbsolutePath();

        CommandLine cmdLine = CommandLine.parse(line);
        cmdLine.addArgument("compile");
        DefaultExecutor executor = new DefaultExecutor();

        if (timeout > 0) {
            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
            executor.setWatchdog(watchdog);
        }

        executor.setExitValue(0);
        executor.setWorkingDirectory(project.getBasedir());
        try {
            executor.execute(cmdLine, getEnvironment());
        } catch (IOException e) {
            throw new MojoExecutionException("Error during compilation", e);
        }
    }
}
