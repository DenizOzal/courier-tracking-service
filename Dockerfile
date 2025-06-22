FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/courier-tracking-app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]