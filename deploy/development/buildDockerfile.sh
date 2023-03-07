
# first rum ./mvnw install

docker build --build-arg JAR_FILE=target/*.jar -t demo-app .

docker run --name demo-api --rm -p 8080:8080 --network demo dimitarkolevdocker/demo-app:1.0.0
