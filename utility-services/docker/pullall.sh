#!/bin/bash
# Pull docker images for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

printf "\nDocker: Pulling Spring Cloud Config Service\n"
sh ../spring-cloud-config/pull.sh
printf "\nDocker: Pulling Eureka Service\n"
sh ../eureka/pull.sh
printf "\nDocker: Pulling Spring Cloud Gateway Service\n"
sh ../spring-cloud-gateway/pull.sh
