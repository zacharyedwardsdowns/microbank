# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/customer

# Run the customer service.
FROM openjdk:17-oracle
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
CMD java -jar -Dspring.config.import=configserver:http://microbank-spring-cloud-config:8888 -Deureka.client.serviceurl.defaultzone=http://microbank-eureka:8761/eureka ./customer*.jar