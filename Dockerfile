FROM maven:3.8.6

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn -Dprofile.active=prod clean install

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend.jar

RUN rm -rf target/

ENTRYPOINT ["java","-jar","/backend.jar"]
