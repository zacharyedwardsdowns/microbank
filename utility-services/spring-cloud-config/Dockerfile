# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/spring-cloud-config

# Run the config server.
FROM openjdk:17-oracle
ARG PASS
ARG URI
ENV URI=${URI}
ENV PASS=${PASS}
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
CMD ["sh", "-c", "java -jar -Duri=$URI -Dpass=$PASS ./spring-cloud-config*.jar"]