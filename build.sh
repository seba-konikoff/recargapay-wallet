./gradlew clean

./gradlew build

podman build -t recargapay-wallet:latest .

podman-compose up
