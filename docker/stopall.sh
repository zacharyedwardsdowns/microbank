# Stop and remove running containers for all MicroBank services.

echo -e "\nStopping containers...\n"
# shellcheck disable=SC2046
docker container stop $(docker container ls -q --filter name=microbank-*)
echo -e "\nRemoving containers...\n"
# shellcheck disable=SC2046
docker container rm $(docker container ls -a -q --filter name=microbank-*)