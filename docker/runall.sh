#!/bin/bash
# Run docker files for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

# Begin running services.
sh ../utility-services/docker/runall.sh

printf "\nStarting customer...\n"
sh ../customer/run.sh

printf "\nStarting angular...\n"
sh ../angular/run.sh

printf "\nAll containers started.\n\n"