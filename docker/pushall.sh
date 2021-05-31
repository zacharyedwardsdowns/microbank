# Push docker images for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
cd ..

sh utility-services/docker/pushall.sh
echo -e "\nDocker: Pushing Angular Service"
sh angular/push.sh
echo -e "\nDocker: Pushing Customer Service"
sh customer/push.sh