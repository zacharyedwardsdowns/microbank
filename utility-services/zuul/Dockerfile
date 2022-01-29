# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/zuul

# Run the zuul server.
FROM alpine:latest
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN apk add --no-cache openjdk11-jre
COPY ./build/libs/*.jar ${workingDirectory}
CMD java -jar -Deureka.client.serviceurl.defaultzone=http://microbank-eureka:8761/eureka ./zuul*.jar