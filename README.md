# Java Project for Demonstration

This project contains some simple, generic Java Spring Boot code in a Gradle project used to demonstrate building Java into containers.

Stella is a standard Spring Boot application. The project was created using [Spring Initializer](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.7.0&packaging=jar&jvmVersion=17&groupId=com.dijure.stella&artifactId=stella&name=stella&description=Hey%20Stella!&packageName=com.dijure.stella&dependencies=native) using Java 17, Gradle, and the Native Image extension. The main performs a simple task. Marlon Brando is famous for his passionate line ["Hey Stella!"](https://youtu.be/S1A0p0F_iH8?t=28) in Tennessee Williams, _A Streetcar named Desire_. This application searches the script for the number of times the Stella name is recited. The wee bit of code is [here](https://github.com/javajon/stella/blob/d100854903b1435d90c79744cfed2c493a8f0267/src/main/java/com/dijure/stella/StellaApplication.java#L15).

See the Katacoda scenario for [Distilled JRE Apps in Containers](https://www.katacoda.com/javajon/courses/kubernetes-containers) to learn how it is used.

Relative topics tags:

Java, Gradle, Spring Boot, Quarkus, Containers, Native Image, OCI, GraalVM, Kubernetes, Microservices


## Why the jlink packaging uses jdeps, not the beryx Gradle plugin

`packaging/Dockerfile-multi-stage-jlink` builds a custom Java runtime with `jdeps` and
`jlink` directly. It used to drive the beryx jlink Gradle plugin from a separate
`build-jlink.gradle`, and two things ended that.

Gradle 9 removed `--build-file` and `-b`, so the separate build file could no longer be
selected at all - the command in the lab was silently building the wrong project.

More fundamentally, the plugin puts the application on the MODULE path: a `module-info.java`
and a merged module. A modern Spring Boot application will not produce a merged module
without a fight. The attempt reports a dozen `Cannot find module exporting ...` errors for
optional dependencies of logback and netty that are never touched at runtime. Spring Boot
3.5 and 4.1 fail the same way, so this is not a version to wait out.

jdeps and jlink do the same job with none of that. The application stays on the classpath,
where Spring Boot expects it. jdeps reads the built jar and reports which JDK modules it
genuinely reaches; jlink assembles a runtime containing exactly those.

Measured on Java 25: eleven modules, and a runtime of 87 MB against a full JDK of 384 MB.
