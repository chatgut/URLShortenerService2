FROM eclipse-temurin:20-jre-jammy
#RUN mvn clean package

COPY target/*.jar app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
