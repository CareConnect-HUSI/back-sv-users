FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean install

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY .env .env

EXPOSE 8088

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=8088"]