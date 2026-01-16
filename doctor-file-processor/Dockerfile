FROM eclipse-temurin:23.0.2_7-jre
WORKDIR /
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
