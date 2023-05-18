#FROM container-registry.oracle.com/graalvm/native-image:latest as graalvm
#
#RUN microdnf -y install wget unzip zip findutils tar
#
#COPY . /app
#WORKDIR /app
#
#RUN \
#    curl -s "https://get.sdkman.io" | bash; \
#    source "$HOME/.sdkman/bin/sdkman-init.sh"; \
#    sdk install maven; \
#    mvn package -Pnative native:compile -DskipTests
#
#FROM container-registry.oracle.com/os/oraclelinux:9-slim
#
#EXPOSE 8001
#COPY --from=graalvm app/target/imageService /app
#
#ENTRYPOINT ["/app"]

FROM maven:3.9-eclipse-temurin-20 as builder
COPY src /app/src
COPY pom.xml /app
RUN mvn --file /app/pom.xml clean package
WORKDIR application
ARG JAR_FILE=/app/target/*.jar
RUN cp ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:20-jre
RUN adduser --system --group spring
USER spring:spring
ENV PORT 8080
EXPOSE 8004
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

