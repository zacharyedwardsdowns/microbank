# Pull docker images for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
cd ..

sh utility-services/docker/pullall.sh
echo -e "\nDocker: Pulling Angular Service"
sh angular/pull.sh
echo -e "\nDocker: Pulling Customer Service"
sh customer/pull.sh