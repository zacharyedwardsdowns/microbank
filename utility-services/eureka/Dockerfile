# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/eureka

# Run the eureka server.
FROM eclipse-temurin:17-jre
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
ENTRYPOINT ["java", "-jar", "eureka.jar"]