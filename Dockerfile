FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /application
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /application
COPY --from=builder /application/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]