FROM openjdk:21-jdk
LABEL maintainer="dlckdrms0517@gmail.com"

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]