FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install -DskipTests=true

FROM openjdk:17-jdk-slim

EXPOSE 8080

ENV JDBC_DATABASE_PASSWORD=${JDBC_DATABASE_PASSWORD}
ENV JDBC_DATABASE_URL=${JDBC_DATABASE_URL}
ENV JDBC_DATABASE_USERNAME=${JDBC_DATABASE_USERNAME}

RUN echo "senha: $JDBC_DATABASE_PASSWORD usuario: $JDBC_DATABASE_USERNAME url: $JDBC_DATABASE_URL"

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/application-heroku.yml"] 
