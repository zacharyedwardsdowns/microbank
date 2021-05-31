# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/customer

# Run the customer service.
FROM alpine:3.12
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN apk add --no-cache openjdk11-jre
COPY ./build/libs/*.jar ${workingDirectory}
CMD java -jar -Dspring.cloud.config.uri=http://microbank-spring-cloud-config:8888 -Deureka.client.serviceurl.defaultzone=http://microbank-eureka:8761/eureka ./customer*.jar