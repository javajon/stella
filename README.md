# Java Project for Demonstration

This project contains some simple, generic Java Spring Boot code in a Gradle project used to demonstrate building Java into containers.

Stella is a standard Spring Boot application. The project was created using [Spring Initializer](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.6.7-SNAPSHOT&packaging=jar&jvmVersion=17&groupId=com.dijure.stella&artifactId=stella&name=stella&description=Hey%20Stella!&packageName=com.dijure.stella&dependencies=native) using Java 17, Gradle, and the Native Image extension. The main performs a simple task. Marlon Brando is famous for his passionate line ["Hey Stella!"](https://youtu.be/S1A0p0F_iH8?t=28) in Tennessee Williams, _A Streetcar named Desire_. This application searches the script for the number of times the Stella name is recited. The wee bit of code is [here](https://github.com/javajon/stella/blob/d100854903b1435d90c79744cfed2c493a8f0267/src/main/java/com/dijure/stella/StellaApplication.java#L15).

See the Katacoda scenario for [Distilled JRE Apps in Containers](https://www.katacoda.com/javajon/courses/kubernetes-containers) to learn how it is used.

Relative topics tags:

Java, Gradle, Spring Boot, Quarkus, Containers, Native Image, OCI, GraalVM, Kubernetes, Microservices
