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
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generate Eclipse project files
 * 
 * @goal eclipse
 */
public class Play2EclipseMojo extends AbstractPlay2SimpleMojo {

	@Override
	protected void addCommandLineArgs(CommandLine cmdLine) {
		cmdLine.addArgument("eclipse");
	}

	@Override
	protected void onExecutionException(Exception e) throws MojoExecutionException {
		throw new MojoExecutionException("Error during Eclipse project file generation", e);
	}

}
