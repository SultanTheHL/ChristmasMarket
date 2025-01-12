
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

RUN chmod +x gradlew

RUN ./gradlew clean build --no-daemon

RUN cp build/libs/*-SNAPSHOT.jar app.jar || cp build/libs/*-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
