FROM amazoncorretto:17-alpine3.21-jdk AS builder
WORKDIR /app
COPY gradle /app/gradle
COPY gradlew build.gradle settings.gradle /app/
RUN chmod +x ./gradlew
COPY src /app/src
RUN ./gradlew build --no-daemon
FROM amazoncorretto:17-alpine3.21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]