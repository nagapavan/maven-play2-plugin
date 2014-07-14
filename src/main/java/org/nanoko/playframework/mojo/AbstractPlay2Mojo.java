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


import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;
import java.util.*;

/**
 * Common parent of all Play 2 Mojo
 */
public abstract class AbstractPlay2Mojo extends AbstractMojo {

    public static final String ENV_PLAY2_HOME = "PLAY2_HOME";
    public static final String PLAY2_ARG_FORMAT = "-D%s=%s";
    /**
     * The list of system properties excluded when copying them to the `play` command line.
     */
    private static final List<String> EXCLUDED_PROPERTIES = Arrays.asList(
            "java.runtime.name",
            "sun.boot.library.path",
            "java.vm.version",
            "user.country.format",
            "com.apple.mrj.application.apple.menu.about.name",
            "java.vm.vendor",
            "java.vendor.url",
            "path.separator",
            "java.vm.name",
            "file.encoding.pkg",
            "user.country",
            "sun.java.launcher",
            "sun.os.patch.level",
            "java.vm.specification.name",
            "user.dir",
            "java.runtime.version",
            "java.awt.graphicsenv",
            "java.endorsed.dirs",
            "os.arch",
            "java.io.tmpdir",
            "line.separator",
            "java.vm.specification.vendor",
            "os.name",
            "tools.jar",
            "sun.jnu.encoding",
            "script.name",
            "java.library.path",
            "sun.awt.enableExtraMouseButtons",
            "java.specification.name",
            "java.class.version",
            "sun.management.compiler",
            "os.version",
            "http.nonProxyHosts",
            "user.home",
            "user.timezone",
            "java.awt.printerjob",
            "file.encoding",
            "java.specification.version",
            "java.class.path",
            "user.name",
            "java.vm.specification.version",
            "sun.java.command",
            "java.home",
            "sun.arch.data.model",
            "user.language",
            "java.specification.vendor",
            "user.language.format",
            "awt.toolkit",
            "java.vm.info",
            "java.version",
            "java.ext.dirs",
            "sun.boot.class.path",
            "java.vendor",
            "file.separator",
            "java.vendor.url.bug",
            "sun.font.fontmanager",
            "sun.io.unicode.encoding",
            "sun.cpu.endian",
            "socksNonProxyHosts",
            "ftp.nonProxyHosts",
            "sun.cpu.isalist",
            // List related to Maven source
            "maven.home",
            "guice.disable.misplaced.annotation.check",
            "classworlds.conf"
    );

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;
    /**
     * The maven session.
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    MavenSession session;
    /**
     * Directory containing the build files.
     *
     * @parameter expression="${project.build.directory}"
     */
    File buildDirectory;
    /**
     * Base directory of the project.
     *
     * @parameter expression="${basedir}"
     */
    File baseDirectory;
    /**
     * Maven ProjectHelper.
     *
     * @component
     * @readonly
     */
    MavenProjectHelper projectHelper;
    /**
     * The PLAY2_HOME path is taken from this setting, if not found as a Java system property (-DPLAY2_HOME).
     * Refers to the PLAY2_HOME environment variable by default.
     * <p/>
     * So that means that the PLAY2_HOME can be given using:
     * <ol>
     * <li>A system variable defined by the system or with <tt>-DPLAY2_HOME=...</tt></li>
     * <li>The <tt>play2Home</tt> configuration property</li>
     * <li>The PLAY2_HOME environment property</li>
     * </ol>
     *
     * @parameter expression="${env.PLAY2_HOME}"
     */
    String play2Home;
    /**
     * The name of the executable for building the play project (which ultimately invokes SBT).
     * Prior to Play 2.3 the executable name was simply <tt>play</tt>.  From Play 2.3 onwards,
     * the executable name can be either <tt>activator</tt> or simply <tt>sbt</tt> directly.
     * <p/>
     * This value is defaulted to <tt>play</tt> for backwards compatibility
     *
     * @parameter default-value="play" expression="${env.SBT_EXECUTABLE_NAME}"
     */
    String executableName = "play";
    /**
     * Sets a timeout to the <tt>play</tt> invocation (in milliseconds).
     * If not set (or set to <tt>-1</tt>, the plugin waits until the underlying <tt>play</tt> process completes.
     * If set, the plugin kills the underlying <tt>play</tt> process when the timeout is reached, and it fails the build.
     *
     * @parameter default-value="-1" expression="${play2timeout}"
     */
    long timeout;
    /**
     * Allows customization of the play execution System properties.
     *
     * @parameter
     */
    Properties play2SystemProperties = new Properties();
    /**
     * Stored the play 2 executable once found to avoid multiple searches.
     */
    private File play2executable;

    /**
     * Checks whether the current operating system is Windows.
     * This check use the <tt>os.name</tt> system property.
     *
     * @return <code>true</code> if the os is windows, <code>false</code> otherwise.
     */
    /* package */
    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Gets the specified <tt>PLAY2_HOME</tt> location.
     * This method checks in this order:
     * <ul>
     * <li>the PLAY2_HOME system variable</li>
     * <li>the <tt>play2Home</tt> settings</li>
     * <li>the PLAY2_HOME environment variable</li>
     * </ul>
     * If none is set, this method throws an exception.
     *
     * @return the play2 location or <code>null</code> if not specified.
     */
    public String getPlay2Home() {
        // First check, system variable
        String home = System.getProperty(ENV_PLAY2_HOME);
        if (home != null && !home.isEmpty()) {
            getLog().debug("Get Play2 home from system variable");
            return home;
        }

        // Second check, the setting configuration
        if (play2Home != null && !play2Home.isEmpty()) {
            getLog().debug("Get Play2 home from settings");
            return play2Home;
        }

        // Third check, environment variable
        home = System.getenv(ENV_PLAY2_HOME);
        if (home != null && !home.isEmpty()) {
            getLog().debug("Get Play2 home from environment");
            return home;
        }

        return null;
    }

    public File getPlay2() throws MojoExecutionException {
        // Do we have a cached value ?
        if (play2executable != null) {
            return play2executable;
        }

        File play2 = null;

        // Either PLAY2_HOME is defined or not.
        // In the first case, we're looking for PLAY2_HOME/play[.bat]
        // In the second case we iterate over the PATH.
        String path = getPlay2Home();
        if (path != null) {
            if (isWindows()) {
                play2 = new File(path, executableName + ".bat");
            } else {
                play2 = new File(path, executableName);
            }
            if (play2.isFile()) {
                play2 = manageHomebrew(play2);
            } else {
                throw new MojoExecutionException(ENV_PLAY2_HOME + " system/configuration/environment variable is set " +
                        "to " + path + " but can't find the 'play' executable");
            }
        } else {
            getLog().info("Looking for '" + executableName + "' in the System PATH");
            play2 = findPlay2ExecutableInSystemPath();
        }

        if (play2 == null || !play2.isFile()) {
            throw new MojoExecutionException("Can't find the '" + executableName + "' executable. Set the " + ENV_PLAY2_HOME + " system/" +
                    "configuration/environment variable or check the the 'play' executable is available from the " +
                    "path");
        }

        getLog().debug("Using " + play2.getAbsolutePath());
        play2executable = play2;

        return play2;
    }

    /**
     * Checks whether the given play executable is in a <tt>Homebrew</tt> managed location.
     * Homebrew scripts seems to be an issue for play as the provided play executable from this directory is using a
     * path expecting relative directories. So, we get such kind of error:
     * <br/>
     * <code>
     * /usr/local/Cellar/play/2.0/libexec/play: line 51:
     * /usr/local/Cellar/play/2.0/libexec//usr/local/Cellar/play/2.0/libexec/../libexec/framework/build:
     * No such file or directory
     * </code>
     * <br/>
     * In this case we substitute the play executable with the one installed by Homebrew but working correctly.
     *
     * @param play2 the found play2 executable
     * @return the given play2 executable except if Homebrew is detected, in this case <tt>/usr/local/bin/play</tt>.
     */
    private File manageHomebrew(File play2) {
        if (play2.getAbsolutePath().contains("/Cellar/" + executableName + "/")) {
            getLog().info("Homebrew installation of " + executableName + " detected");
            // Substitute the play executable by the homebrew one.
            File file = new File("/usr/local/bin/" + executableName);
            if (!file.exists()) {
                getLog().error("Homebrew installation detected, but no " + executableName + " executable in /usr/local/bin");
            } else {
                return file;
            }
        }
        return play2;
    }

    private File findPlay2ExecutableInSystemPath() {
        String play2 = executableName;
        if (isWindows()) {
            play2 = executableName + ".bat";
        }
        String systemPath = System.getenv("PATH");

        // Fast failure if we don't have the PATH defined.
        if (systemPath == null) {
            return null;
        }

        String[] pathDirs = systemPath.split(File.pathSeparator);

        for (String pathDir : pathDirs) {
            File file = new File(pathDir, play2);
            if (file.isFile()) {
                return file;
            }
        }
        // Search not successful.
        return null;
    }

    /**
     * Gets the execution environment.
     * This method builds a map containing the Maven properties to give to SBT invocations.
     * It contains maven project data (GAV), pom properties, and system properties.
     *
     * @return the map of properties (<code>key -&gt; value</code>)
     */
    public Map<String, String> getEnvironment() {
        Map<String, String> env = new HashMap<String, String>();

        // Environment variables
        env.putAll(System.getenv());

        // Build properties.
        env.put("project.groupId", project.getGroupId());
        env.put("project.artifactId", project.getArtifactId());
        env.put("project.version", project.getVersion());

        // Pom properties
        Properties props = project.getProperties();
        if (props != null) {
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                env.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        // Environment properties
        Map<String, String> environment = System.getenv();
        for (String k : environment.keySet()) {
            env.put(k, environment.get(k));
        }

        // System properties
        props = System.getProperties();
        if (props != null) {
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                env.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        getLog().debug("Environment built : " + env);
        return env;
    }

    /**
     * @return the maven project
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * @return the build directory (generally <tt>target</tt>)
     */
    public File getBuildDirectory() {
        return buildDirectory;
    }

    /**
     * @return the array of play2 system properties arguments.
     * The final execution line looks like: <tt>play -Dproperty=value -Dproperty2=value2 run/test</tt>
     */
    public String[] getPlay2SystemPropertiesArguments() {
        Set<String> args = new HashSet<String>();
        for (Map.Entry<Object, Object> entry : play2SystemProperties.entrySet()) {
            args.add(String.format(PLAY2_ARG_FORMAT, entry.getKey(), entry.getValue()));
        }
        addProxiesSystemProperties(args);
        appendCurrentSystemProperties(args);

        return args.toArray(new String[0]);
    }

    /**
     * Iterates over the system properties and append them to the list of system properties.
     * Some default system properties are excluded.
     *
     * @param system the system properties.
     */
    private void appendCurrentSystemProperties(Set<String> system) {
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (!EXCLUDED_PROPERTIES.contains(entry.getKey())) {
                system.add(String.format(PLAY2_ARG_FORMAT, entry.getKey(), entry.getValue()));
            }
        }
    }

    /**
     * Appends the proxy settings (extracted from the Maven settings) to the system properties list.
     * It adds the following properties:
     * <code>
     *        -Dhttp.proxyHost=yourserver -Dhttp.proxyPort=8080 -Dhttp.proxyUser=username -Dhttp.proxyPassword=password
     * </code>
     *
     * @param system the system properties.
     */
    private void addProxiesSystemProperties(Set<String> system) {
        // Skip if there is no proxy.
        if (session.getSettings().getActiveProxy() == null) {
            return;
        }
        // Append proxy settings if configured for http
        String proxyProtocol = session.getSettings().getActiveProxy().getProtocol();
        String proxyHost = session.getSettings().getActiveProxy().getHost();
        int proxyPort = session.getSettings().getActiveProxy().getPort();
        String proxyUser = session.getSettings().getActiveProxy().getUsername();
        String proxyPassword = session.getSettings().getActiveProxy().getPassword();
        if ("http".equals(proxyProtocol) && proxyHost != null) {
            system.add(String.format(PLAY2_ARG_FORMAT, "http.proxyHost", proxyHost));
            if (proxyPort != 0) {
                system.add(String.format(PLAY2_ARG_FORMAT, "http.proxyPort", proxyPort));
            }
            if (proxyUser != null) {
                system.add(String.format(PLAY2_ARG_FORMAT, "http.proxyUser", proxyUser));
            }
            if (proxyPassword != null) {
                system.add(String.format(PLAY2_ARG_FORMAT, "http.proxyPassword", proxyPassword));
            }
        }
    }
}
