
# Etapa 1: Compilar el proyecto
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar Spring Boot
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-Dserver.port=${PORT:10000}", "-jar", "app.jar"]
