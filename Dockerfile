
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

RUN chmod +x gradlew

RUN ./gradlew clean build --no-daemon

RUN ls build/libs/*.jar && cp build/libs/*.jar app.jar || (echo "JAR not found!" && exit 1)

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
