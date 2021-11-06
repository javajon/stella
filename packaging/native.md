## Using the nativeBuild Task

To build a native image there are just a few steps to follow. The nativeBuild task will build a native container if you are on Linux, but it will failed with this error message:

> No compatible toolchains found for request filter: {languageVersion=11, vendor=matching('GraalVM')

If you have not installed Java Graal. The optimal way to do this is using SDKMAN.

`curl -s "https://get.sdkman.io" | bash && source "/root/.sdkman/bin/sdkman-init.sh" && sdk version`

You can list the available Java SDK/JREs that SDKMAN cna manage:

`sdk list java`

For the missing Java Graal install the latest for Java 11.

`sdk install java 21.3.0.r11-grl`

After installation it will report Graal as being the default installation for Java.

`java --version`

With Graal installed you can not create a native image from theJava code with this Gradle task:

`./gradlew nativeBuild`

Once created, you can run the binary and see its fast execution speed:

`build/native/nativeBuild/stella`

There is also a Dockerfile that will package this native image into a into container image:

`docker build -t stella-local -f packaging/Dockerfile-native-local .`
