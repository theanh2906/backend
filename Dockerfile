FROM gradle:7.6-jdk17 AS build

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle clean build --no-daemon

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/backend.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]