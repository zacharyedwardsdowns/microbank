#!/bin/bash
# Pull docker images for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
cd ..

sh utility-services/docker/pullall.sh
printf "\nDocker: Pulling Angular Service\n"
sh angular/pull.sh
printf "\nDocker: Pulling Customer Service\n"
sh customer/pull.sh