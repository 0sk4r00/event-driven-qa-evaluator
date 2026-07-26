# STAGE 1: Kompilacja i budowanie (Builder)
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Kopiujemy pom.xml i pobieramy zależności (wykorzystujemy cache Dockera)
COPY pom.xml .
RUN mvn dependency:go-offline

# Kopiujemy kod źródłowy i budujemy plik .jar
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Uruchomienie (Lekki obraz produkcyjny)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Kopiujemy TYLKO gotowy plik JAR z poprzedniego etapu
COPY --from=builder /app/target/*.jar app.jar

# Wyciągamy ograniczenia pamięci bezpośrednio z flag JVM
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseG1GC"

EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]