# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/customer

# Run the customer service.
FROM openjdk:17-oracle
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "customer.jar"]