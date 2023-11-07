#!/bin/bash
# Build all MicroBank utility services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

cd utility-services || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
sh buildall.sh

printf "\nBuilding Angular Service\n"
cd ../angular || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
ng build

printf "\nBuilding Customer Service\n"
cd ../customer || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle clean build