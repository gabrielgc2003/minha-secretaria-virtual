# =========================
# STAGE 1 — BUILD
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas o pom primeiro (cache de dependências)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código
COPY src ./src

# Gera o jar
RUN mvn clean package -DskipTests
RUN ls -R src/main/resources/db/migration/
RUN jar -tf target/*.jar | grep db/migration/

# =========================
# STAGE 2 — RUNTIME
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o jar gerado no build
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring
EXPOSE 8080

# Variáveis de JVM seguras para container
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Start da aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
