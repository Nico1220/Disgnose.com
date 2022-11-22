FROM maven:4.0.0-jdk-18
COPY ./ ./
RUN mvn clean package
CMD ["java", "-jar", "target/api-v0.0.1.jar"]