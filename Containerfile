FROM amazoncorretto:24.0.2-al2023-headless
WORKDIR /app
COPY build/libs/wallet-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]