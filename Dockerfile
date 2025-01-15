FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y wget && \
    wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz && \
    tar -xvzf dockerize-linux-amd64-v0.6.1.tar.gz && \
    mv dockerize /usr/local/bin

WORKDIR /app

COPY ./target/to-do-0.0.1-SNAPSHOT.jar /app/tasker.jar

CMD ["dockerize", "-wait", "tcp://db:3306", "-timeout", "25s", "java", "-jar", "tasker.jar"]