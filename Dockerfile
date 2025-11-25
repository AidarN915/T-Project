# Используем Maven + JDK
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходники
COPY src ./src

# Сборка jar
RUN mvn clean package spring-boot:repackage


# --- второй слой для минимального образа ---
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный jar из предыдущего слоя
COPY --from=build /app/target/T-project-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]