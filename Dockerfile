FROM eclipse-temurin:17-jre-jammy
#RUN mvn clean package

COPY target/*.jar app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
#FROM maven:3.9-eclipse-temurin-20 as builder
#COPY src /app/src
#COPY pom.xml /app
#RUN mvn --file /app/pom.xml clean package
#WORKDIR application
#ARG JAR_FILE=/app/target/*.jar
#RUN cp ${JAR_FILE} application.jar
#RUN java -Djarmode=layertools -jar application.jar extract
#
#FROM eclipse-temurin:20-jre
#RUN adduser --system --group spring
#USER spring:spring
#ENV PORT 8080
#EXPOSE 8080
#WORKDIR application
#COPY --from=builder application/dependencies/ ./
#COPY --from=builder application/snapshot-dependencies/ ./
#COPY --from=builder application/spring-boot-loader/ ./
#COPY --from=builder application/application/ ./
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
