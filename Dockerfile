FROM maven:3.8.6-openjdk-18-slim as build
# maven jar erstellen
COPY . .
RUN mvn clean package -DskipTests
## taget jar
FROM openjdk:19-alpine
COPY --from=build /target .

CMD ["java", "-jar", "haupt-store-v0.0.1.jar"]