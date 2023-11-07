#!/bin/bash
# Clean build directories for all MicroBank utility services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

printf "\nCleaning Spring Cloud Config Service\n"
cd spring-cloud-config || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle clean

printf "\nCleaning Eureka Service\n"
cd ../eureka || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle clean

printf "\nCleaning Spring Cloud Gateway Service\n"
cd ../spring-cloud-gateway || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle clean