# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/spring-cloud-gateway

# Run the spring-cloud-gateway server.
FROM openjdk:17-oracle
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build/libs/*.jar ${workingDirectory}
CMD java -jar -Deureka.client.serviceurl.defaultzone=http://microbank-eureka:8761/eureka ./spring-cloud-gateway*.jar