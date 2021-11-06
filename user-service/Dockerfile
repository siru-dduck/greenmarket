FROM gradle:6.9-jdk11 AS builder

WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN gradle bootJar

FROM azul/zulu-openjdk:11
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8150
ENTRYPOINT [ "java", "-jar", "/app.jar" ]