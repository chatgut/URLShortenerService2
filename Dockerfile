FROM eclipse-temurin:20-jre-jammy
RUN apt-get update && apt-get install -y maven


COPY target/*.jar app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

FROM mysql:latest

ENV MYSQL_ROOT_PASSWORD=secret-pw \
    MYSQL_ROOT_HOST='%' \
    MYSQL_DATABASE=demo \
    MYSQL_USER=developer \
    MYSQL_PASSWORD=password

EXPOSE 3306
