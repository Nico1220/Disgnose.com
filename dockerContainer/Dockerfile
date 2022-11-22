FROM maven:3.8.6 as build
# maven jar erstellen
COPY ./src src
COPY ./pom.xml .
RUN mvn -f pom.xml clean package install

FROM openjdk:8-jre
## taget jar
COPY --from=build /target /opt/target
WORKDIR /target
CMD ["java", "-jar", "haupt-store-v.0.0.1.jar"]

#FROM maven:3.8.6 as build
## maven jar erstellen
#RUN mvn -f pom.xml clean package install
### taget jar
#COPY --from=build /target .
#CMD ["java", "-jar", "haupt-store-v.0.0.1.jar"]