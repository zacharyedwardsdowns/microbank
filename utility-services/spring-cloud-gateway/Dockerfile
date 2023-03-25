# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/spring-cloud-gateway

# Run the spring-cloud-gateway server.
FROM eclipse-temurin:17-jre
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "spring-cloud-gateway.jar"]