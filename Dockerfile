FROM openjdk:17-jdk
WORKDIR /app
COPY target/dev-drones-0.1.0.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","dev-drones-0.1.0.jar"]