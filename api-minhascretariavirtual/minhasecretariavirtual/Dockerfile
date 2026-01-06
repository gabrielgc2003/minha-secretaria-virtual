# =========================
# STAGE 1 — BUILD
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Otimização de Cache: Baixa dependências antes de copiar o código
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Gera o jar (skip tests para agilidade no build da imagem,
# assumindo que testes já rodaram na pipeline de CI)
RUN mvn clean package -DskipTests

# =========================
# STAGE 2 — RUNTIME
# =========================
FROM eclipse-temurin:21-jre-alpine

# Instala tzdata para configurar o fuso horário (necessário no Alpine)
RUN apk add --no-cache tzdata
ENV TZ=America/Sao_Paulo

WORKDIR /app

# Criação de usuário não-root (Segurança)
# Cria um grupo 'spring' e um usuário 'spring'
RUN addgroup -S spring && adduser -S spring -G spring

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Define permissão para o usuário spring
RUN chown spring:spring app.jar

# Troca para o usuário seguro
USER spring

EXPOSE 8080

# Configurações da JVM
# -Djava.security.egd acelera a geração de números aleatórios na inicialização
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]