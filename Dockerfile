FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .

RUN mvn -pl app-web -am clean install -DskipTests \
    && mvn -f app-web/pom.xml package org.springframework.boot:spring-boot-maven-plugin:4.0.3:repackage -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/app-web/target/app-web-1.0-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70.0 -XX:+UseSerialGC"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
