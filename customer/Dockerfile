# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/customer

# The maven build stage of the service.
FROM maven:3.6.3-jdk-8-slim as build
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn package

# Run the customer service.
FROM alpine:3.7
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN apk add --no-cache openjdk8-jre
COPY --from=build ${workingDirectory}/target/*.jar ${workingDirectory}
CMD java -jar ./customer*.jar