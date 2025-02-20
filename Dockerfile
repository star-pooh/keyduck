FROM amazoncorretto:17-alpine3.21-jdk AS builder
WORKDIR /myapp
COPY gradle /myapp/gradle
COPY gradlew build.gradle settings.gradle /myapp/
RUN chmod +x ./gradlew
COPY src /myapp/src
RUN ./gradlew build --no-daemon --stacktrace
FROM amazoncorretto:17-alpine3.21-jdk
WORKDIR /myapp
COPY --from=builder /myapp/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]