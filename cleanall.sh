#!/bin/bash
# Clean build directories for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

cd utility-services || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
sh cleanall.sh

printf "\nCleaning Angular Service\n"
cd ../angular || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
if [ -d "dist" ]; then
  rm -r dist
fi
printf "\n- DIST DIRECTORY REMOVED -\n"

printf "\nCleaning Customer Service\n"
cd ../customer || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle clean