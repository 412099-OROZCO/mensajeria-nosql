# Etapa 1: construir el JAR con Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY docs ./docs
RUN mvn clean package

# Etapa 2: correr la app con Java
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/classes /app
EXPOSE 8080
CMD ["java", "com.bytechat.Main"]
