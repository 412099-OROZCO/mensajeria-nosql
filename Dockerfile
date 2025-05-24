# Etapa 1: Build del JAR con todas las dependencias
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY docs ./docs
RUN mvn clean package

# Etapa 2: Ejecutar el JAR ensamblado usando base confiable
FROM openjdk:17-slim

# Instalar certificados necesarios (aunque esta base ya los suele tener)
RUN apt-get update && apt-get install -y ca-certificates && apt-get clean

WORKDIR /app
COPY --from=build /app/target/mensajeria-nosql-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY --from=build /app/docs /app/docs

EXPOSE 8080
CMD ["java", "-Dhttps.protocols=TLSv1.2", "-jar", "app.jar"]


