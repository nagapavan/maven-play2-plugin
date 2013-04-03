/*
 * Copyright 2013 OW2 Nanoko Project Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.nanoko.playframework.mojo;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Helper for very simple Mojos
 */
public abstract class AbstractPlay2SimpleMojo extends AbstractPlay2Mojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		String line = this.getPlay2().getAbsolutePath();

		CommandLine cmdLine = CommandLine.parse(line);
		this.addCommandLineArgs(cmdLine);

		DefaultExecutor executor = new DefaultExecutor();
		if (this.timeout > 0) {
			ExecuteWatchdog watchdog = new ExecuteWatchdog(this.timeout);
			executor.setWatchdog(watchdog);
		}

		executor.setExitValue(0);
		executor.setWorkingDirectory(this.project.getBasedir());
		try {
			executor.execute(cmdLine, this.getEnvironment());
		} catch (Exception e) {
			this.onExecutionException(e);
		}
	}

	/**
	 * Opportunity for subclasses to populate the command line args
	 * 
	 * @param cmdLine
	 */
	protected abstract void addCommandLineArgs(CommandLine cmdLine);

	/**
	 * Generic handler for execution exceptions. Default implementation throws a
	 * MojoExecutionException.
	 * 
	 * @param e
	 * @throws MojoExecutionException
	 */
	protected void onExecutionException(Exception e) throws MojoExecutionException {
		throw new MojoExecutionException("Error during execution", e);
	}

}
