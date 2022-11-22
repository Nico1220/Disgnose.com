FROM openjdk:18
COPY ./ ./
RUN mvn clean package
CMD ["java", "-jar", "target/api-v0.0.1.jar"]