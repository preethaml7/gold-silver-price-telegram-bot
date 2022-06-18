FROM maven:latest AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /usr/src/app/target/gold-silver-price-telegram-bot-1.0.0.jar /usr/app/gold-silver-price-telegram-bot-1.0.0.jar
ENTRYPOINT ["java","-jar","/usr/app/gold-silver-price-telegram-bot-1.0.0.jar"]
