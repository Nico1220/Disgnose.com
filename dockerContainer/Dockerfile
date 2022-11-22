FROM maven:3.8.6 as build
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# maven jar erstellen

## taget jar

#COPY --from=build  api-v.0.0.1.jar
#CMD ["java", "-jar", "api-v0.0.1.jar"]