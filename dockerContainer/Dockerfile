FROM maven:3.8.6 as build
# maven jar erstellen
RUN mvn clean package install
## taget jar
COPY --from=build /target haupt-store-v.0.0.1.jar
CMD ["java", "-jar", "haupt-store-v.0.0.1.jar"]
