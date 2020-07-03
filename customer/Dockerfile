# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/customer

# The gradle build stage of the service.
FROM gradle:6.5.0-jdk11 as build
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build.gradle ./settings.gradle ./
RUN gradle -q build || return 0
COPY . .
RUN gradle -q build

# Run the customer service.
FROM alpine:3.12
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN apk --no-cache add openjdk11-jre
COPY --from=build ${workingDirectory}/build/libs/*.jar ${workingDirectory}
CMD java -jar ./customer*.jar