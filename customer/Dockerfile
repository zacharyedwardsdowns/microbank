# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/customer

# Run the customer service.
FROM eclipse-temurin:17-jre
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "customer.jar"]