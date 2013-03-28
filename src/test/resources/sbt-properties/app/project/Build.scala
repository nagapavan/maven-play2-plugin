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

import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  def fromEnv(name: String) = System.getenv(name) match {
    case null => None
    case value => Some(value)
  }

  val appName = fromEnv("project.artifactId").getOrElse("my-app")
  val appVersion = fromEnv("project.version").getOrElse("1.0-SNAPSHOT")

  System.out.println("AppName => " + appName + " / " + fromEnv("project.artifactId"))


  val appDependencies = Seq(
    // Add your project dependencies here,
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Target folder
    target <<= baseDirectory / "target",

    // Source folders
    sourceDirectory in Compile <<= baseDirectory / "src/main/java",
    sourceDirectory in Test <<= baseDirectory / "src/test/java",

    confDirectory <<= baseDirectory / "src/main/conf",
    resourceDirectory in Compile <<= baseDirectory / "src/main/conf",

    scalaSource in Compile <<= baseDirectory / "src/main/scala",
    scalaSource in Test <<= baseDirectory / "src/test/scala",

    javaSource in Compile <<= baseDirectory / "src/main/java",
    javaSource in Test <<= baseDirectory / "src/test/java",


    distDirectory <<= baseDirectory / "target/dist",

    playAssetsDirectories := Seq.empty[File],

    // The route file also needs to be updated...
    playAssetsDirectories <+= baseDirectory / "src/main/resources"


  )

}
