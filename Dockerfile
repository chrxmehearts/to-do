FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/to-do-0.0.1-SNAPSHOT.jar /app/tasker.jar

ENTRYPOINT ["java", "-jar", "tasker.jar"]