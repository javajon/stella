Fix Step 7

$
$ less packaging/Dockerfile-multi-stage-native
## Stage 0 : Small stage, just to access a prerequisite dependency of `xargs` utility
FROM debian as resolve-xargs

## Stage 1 : Gradle build produces the application executable jar and custom JRE
FROM quay.io/quarkus/ubi-quarkus-native-image:22.0.0-java17 as builder

# Needed by gradlew, but missing from base ubi images
COPY --from=resolve-xargs /usr/bin/xargs /usr/bin/

WORKDIR /code
COPY --chown=quarkus:quarkus gradlew ./gradlew
COPY --chown=quarkus:quarkus gradle ./gradle
COPY --chown=quarkus:quarkus build-native.gradle ./
COPY --chown=quarkus:quarkus settings.gradle ./
COPY src ./src

# Native executable using Graal
RUN ./gradlew nativeCompile --build-file build-native.gradle


## Stage 2 : Just the facts (binary) ma'am. (https://quarkus.io/guides/building-native-image#using-a-distroless-base-image)
FROM quay.io/quarkus/quarkus-distroless-image:1.0
WORKDIR /opt
COPY --from=builder /code/build/native/nativeCompile/stella stella
CMD ["./stella"]
$ docker image build \
> -f packaging/Dockerfile-multi-stage-native \
> -t stella-e-ms-native:0.0.1 \
> .
Sending build context to Docker daemon  14.83MB
Step 1/14 : FROM debian as resolve-xargs
latest: Pulling from library/debian
dbba69284b27: Pull complete
Digest: sha256:87eefc7c15610cca61db5c0fd280911c6a737c0680d807432c0bd80cd0cca39b
Status: Downloaded newer image for debian:latest
---> d69c6cd3a20d
Step 2/14 : FROM quay.io/quarkus/ubi-quarkus-native-image:22.0.0-java17 as builder
22.0.0-java17: Pulling from quarkus/ubi-quarkus-native-image
a9e23b64ace0: Pull complete
38b71301a1d9: Pull complete
327616603b98: Pull complete
Digest: sha256:a91304397514482bd6fd6b0fce6ff21ad487d59089208462284a8f7aadd7ef30
Status: Downloaded newer image for quay.io/quarkus/ubi-quarkus-native-image:22.0.0-java17
---> b0b510708ecd
Step 3/14 : COPY --from=resolve-xargs /usr/bin/xargs /usr/bin/
---> 2afc5c29555d
Step 4/14 : WORKDIR /code
---> Running in 3deb167065ab
Removing intermediate container 3deb167065ab
---> 6c9df159bc10
Step 5/14 : COPY --chown=quarkus:quarkus gradlew ./gradlew
---> e0db2a337cc6
Step 6/14 : COPY --chown=quarkus:quarkus gradle ./gradle
---> 3f2c7dd93863
Step 7/14 : COPY --chown=quarkus:quarkus build-native.gradle ./
---> 0cc988265f3f
Step 8/14 : COPY --chown=quarkus:quarkus settings.gradle ./
---> 007786415a5c
Step 9/14 : COPY src ./src
---> 6aaaa86661ae
Step 10/14 : RUN ./gradlew nativeCompile --build-file build-native.gradle
---> Running in 4063c244540c
Downloading https://services.gradle.org/distributions/gradle-7.4.1-bin.zip
...........10%...........20%...........30%...........40%...........50%...........60%...........70%...........80%...........90%...........100%

Welcome to Gradle 7.4.1!

Here are the highlights of this release:
- Aggregated test and JaCoCo reports
- Marking additional test source directories as tests in IntelliJ
- Support for Adoptium JDKs in Java toolchains

For more details see https://docs.gradle.org/7.4.1/release-notes.html

Starting a Gradle Daemon (subsequent builds will be faster)

FAILURE: Build failed with an exception.

* What went wrong:
  Gradle could not start your build.
> Cannot create service of type DependencyLockingHandler using method DefaultDependencyManagementServices$DependencyResolutionScopeServices.createDependencyLockingHandler() as there is a problem with parameter #2 of type ConfigurationContainerInternal.
> Cannot create service of type ConfigurationContainerInternal using method DefaultDependencyManagementServices$DependencyResolutionScopeServices.createConfigurationContainer() as there is a problem with parameter #13 of type DefaultConfigurationFactory.
> Cannot create service of type DefaultConfigurationFactory using DefaultConfigurationFactory constructor as there is a problem with parameter #2 of type ConfigurationResolver.
> Cannot create service of type ConfigurationResolver using method DefaultDependencyManagementServices$DependencyResolutionScopeServices.createDependencyResolver() as there is a problem with parameter #1 of type ArtifactDependencyResolver.
> Cannot create service of type ArtifactDependencyResolver using method DependencyManagementBuildScopeServices.createArtifactDependencyResolver() as there is a problem with parameter #4 of type List<ResolverProviderFactory>.
> Could not create service of type VersionControlRepositoryConnectionFactory using VersionControlBuildSessionServices.createVersionControlSystemFactory().
> Failed to create parent directory '/code/.gradle' when creating directory '/code/.gradle/vcs-1'

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.

* Get more help at
  https://help.gradle.org
  Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

See https://docs.gradle.org/7.4.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD FAILED in 7s

FAILURE: Build failed with an exception.

* What went wrong:
  Could not update /code/.gradle/7.4.1/fileChanges/last-build.bin
> /code/.gradle/7.4.1/fileChanges/last-build.bin (No such file or directory)

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.

* Get more help at https://help.gradle.org

Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

See https://docs.gradle.org/7.4.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD FAILED in 8s
The command '/bin/sh -c ./gradlew nativeCompile --build-file build-native.gradle' returned a non-zero code: 1
$ 