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

import java.util.zip.ZipFile

def artifactNameWithoutExtension = "my-app/target/my-app-1.0-SNAPSHOT"

// check the main jar artifact
def jar = new File(basedir, artifactNameWithoutExtension + ".jar");
assert jar.exists();
assert jar.canRead();

// check the zip distribution
def dist = new File(basedir, artifactNameWithoutExtension + ".zip");
assert dist.exists();
assert dist.canRead();

// check the war file
def war = new File(basedir, artifactNameWithoutExtension + ".war");
assert war.exists();
assert war.canRead();

// check that the zip distribution contains my-module and javax.validation
def moduleFound = false
def validationFound = false
def moduleFileName = "my-module-1.0-SNAPSHOT.jar"
def validationJarName = "javax.validation.validation-api-1.0.0.GA.jar"
zip = new ZipFile(dist)
entries = zip.entries()
entries.each
        { entry ->
            if (entry.name.contains(moduleFileName)) {
                moduleFound = true
            }
            if (entry.name.contains(validationFound)) {
                validationFound = true
            }
        }

assert moduleFound
assert validationFound



