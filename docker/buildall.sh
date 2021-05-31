# Build docker images for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

sh ../utility-services/docker/buildall.sh
echo -e "\nDocker: Building Angular Service"
sh ../angular/build.sh
echo -e "\nDocker: Building Customer Service"
sh ../customer/build.sh