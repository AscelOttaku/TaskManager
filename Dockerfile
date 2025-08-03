FROM openjdk:21-jdk-slim-buster
WORKDIR /app
COPY target/TaskManager-0.0.1-SNAPSHOT.jar /app/TaskManager.jar
ENTRYPOINT ["java", "-jar", "TaskManager.jar"]
EXPOSE 8080