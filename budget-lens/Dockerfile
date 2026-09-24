# Download dependencies
FROM eclipse-temurin:26-jdk-alpine AS dependencies
RUN apk add --no-cache maven
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

# Build the app
FROM dependencies AS builder
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:26-jre AS runtime
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]