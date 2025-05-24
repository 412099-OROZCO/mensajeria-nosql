# Etapa 1: Build del JAR con todas las dependencias
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY docs ./docs
RUN mvn clean package

# Etapa 2: Ejecutar el JAR ensamblado
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/mensajeria-nosql-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY --from=build /app/docs /app/docs
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

