play2-maven-plugin
==================

The play2-maven-plugin is used to build Play 2 applications using Maven. Basically, it just a wrapper around the _play_
command. It supports the application compilation, test and packaging.

It defines the _play2_ packaging type and supports the compilation, the test and the packaging of the application. Dependencies
are also managed using Maven.

The documentation is available on :

* http://nanoko-project.github.com/maven-play2-plugin/maven/release/ for the latest release
* http://nanoko-project.github.com/maven-play2-plugin/maven/snapshot/ for the development version

**IMPORTANT**: To use `activator`, you need to set the `SBT_EXECUTABLE_NAME` system property to `activator`:

```
export SBT_EXECUTABLE_NAME=activator
mvn clean install
```

The plugin is licensed under the Apache Software License 2.0.
