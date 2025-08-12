FROM amazoncorretto:24.0.2-al2023-headless
WORKDIR /app
COPY . .
EXPOSE 8080
CMD ["./gradlew", "clean", "build"]
CMD ["java", "-jar", "build/libs/wallet-0.0.1-SNAPSHOT.jar"]