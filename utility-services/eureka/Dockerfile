# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/eureka

# Run the eureka server.
FROM openjdk:17-oracle
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
CMD java -jar ./eureka*.jar