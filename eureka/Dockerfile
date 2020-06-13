# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/eureka

# The gradle build stage of the service.
FROM gradle:6.5.0-jdk8 as build
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY ./build.gradle ./settings.gradle ./
RUN gradle -q build || return 0
COPY . .
RUN gradle -q build

# Run the registration service.
FROM alpine:3.7
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN apk add --no-cache openjdk8-jre
COPY --from=build ${workingDirectory}/build/libs/*.jar ${workingDirectory}
CMD java -jar ./eureka*.jar