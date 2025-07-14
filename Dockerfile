FROM maven:3.8.6 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/backend.jar backend.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "backend.jar"]