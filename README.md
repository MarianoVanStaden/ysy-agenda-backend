![img.png](img.png)## Overview

An example project to demonstrate:

* how to create a Spring Boot REST API (see [article](https://tomgregory.com/building-a-spring-boot-application-in-jenkins/))
* how to run Spring Boot in Docker and publish to Docker Hub (see [article](https://tomgregory.com/building-a-spring-boot-application-in-docker-and-jenkins/))
* how to deploy the Spring Boot application to AWS with CloudFormation

### Testing

`./gradlew test`

### Building (no tests)

`./gradlew assemble`

### Running in Docker (Docker installation expected)

`./gradlew assemble docker dockerRun`

### Stopping Docker container

`./gradlew dockerStop`

