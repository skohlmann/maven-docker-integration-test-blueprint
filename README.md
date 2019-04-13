
Blueprint for Docker based Maven integration tests
==================================================

This blueprint shows what a Maven project can look like that performs an integration test of a Java program running in a Docker Container.

The blueprint works with

* [Docker](https://www.docker.com) (version 18.09.2)<br />Docker must be installed as a prerequisite to run the blueprint.
* Spotify <tt>[dockerfile-maven-plugin](https://github.com/spotify/dockerfile-maven)</tt>
* [testcontainers.org](http://testcontainers.org/) Maven plugin for JUnit 5
* [Maven](http://maven.apache.org) 3.5 at least

As server environment the blueprint uses Project [Helidon.io](https://helidon.io/#/).

The blueprint was tested with Java 12 on macOS 10.14 (Mojave).

Execution
---------

Run

> mvn verify

from command line to execute the integration test.

Current problems
----------------

* The Helidon.io project (version 1.0.2) has a dependency to a not Java Jigsaw compatible module: <tt>module io.helidon.webserver reads package org.newsclub.net.unix from both junixsocket.native.common and junixsocket.common</tt>. Therefore the project doesn't contain  <tt>module-info.java</tt> files.
* The network port of the server is hard bound. It cannot currently be used any free port.

----

Have fun :-)
