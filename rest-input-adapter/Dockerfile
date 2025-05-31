FROM eclipse-temurin:11-jdk

WORKDIR /app
COPY target/rest-input-adapter-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]