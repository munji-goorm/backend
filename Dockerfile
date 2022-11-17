FROM openjdk:20-ea-11-slim-buster
COPY build/libs/backend-0.0.1-SNAPSHOT.jar MunjiGoorm.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "MunjiGoorm.jar"]
