#!/bin/bash
# Run docker files for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

# Check if a network exists, if not create one.
networkSubnet="172.35.6.0/16"
eurekaIp="172.35.6.72"
networkFound="false"

for network in $(docker network ls --format "{{.Name}}")
do
  if [ "$network" = "microbank-network" ]
  then
    networkFound="true"
    break
  fi
done

if [ "$networkFound" = "false" ]
then
  printf "\nCreating the microbank-network...\n"
  docker network create microbank-network --subnet "$networkSubnet"
fi



# Begin running services.
printf "\nStarting spring-cloud-config...\n"
sh ../spring-cloud-config/run.sh

printf "\nStarting eureka...\n"
sh ../eureka/run.sh "$eurekaIp"

printf "\nStarting spring-cloud-gateway...\n"
sh ../spring-cloud-gateway/run.sh

printf "\nAll utility containers started.\n\n"