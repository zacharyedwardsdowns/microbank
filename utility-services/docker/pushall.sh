#!/bin/bash
# Push docker images for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

printf "\nDocker: Pushing Spring Cloud Config Service\n"
sh ../spring-cloud-config/push.sh
printf "\nDocker: Pushing Eureka Service\n"
sh ../eureka/push.sh
printf "\nDocker: Pushing Spring Cloud Gateway Service\n"
sh ../spring-cloud-gateway/push.sh
