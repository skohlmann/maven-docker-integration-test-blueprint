FROM openjdk:latest

MAINTAINER Sascha Kohlmann

RUN mkdir /app
COPY target/libs /app/libs 
COPY target/blueprint-docker-integration.jar /app 

ENTRYPOINT ["/usr/bin/java", "-jar", "/app/blueprint-docker-integration.jar"]
