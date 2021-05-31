# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/eureka

# Run the eureka server.
FROM alpine:3.12
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN apk add --no-cache openjdk11-jre
COPY ./build/libs/*.jar ${workingDirectory}
CMD java -jar ./eureka*.jar