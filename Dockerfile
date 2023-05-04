FROM eclipse-temurin:20-jre-jammy
RUN apt-get update && apt-get install -y maven

COPY target/*.jar app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]