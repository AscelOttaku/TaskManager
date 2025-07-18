FROM openjdk:21-jdk-slim-buster
WORKDIR /app
COPY target/JobSearch-0.0.1-SNAPSHOT.jar /app/JobSearch.jar
ENTRYPOINT ["java", "-jar", "JobSearch.jar"]
EXPOSE 8081