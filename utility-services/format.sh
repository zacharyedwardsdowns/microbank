#!/bin/bash
# Format all MicroBank utility java files using google java format.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

printf "\nFormatting Spring Cloud Config Service\n"
cd spring-cloud-config || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle spotlessJavaApply

printf "\nFormatting Eureka Service\n"
cd ../eureka || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle spotlessJavaApply

printf "\nFormatting Spring Cloud Gateway Service\n"
cd ../spring-cloud-gateway || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle spotlessJavaApply