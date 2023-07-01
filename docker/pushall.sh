# Push docker images for all MicroBank services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
cd ..

sh utility-services/docker/pushall.sh
printf "\nDocker: Pushing Angular Service\n"
sh angular/push.sh
printf "\nDocker: Pushing Customer Service\n"
sh customer/push.sh