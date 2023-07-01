# Stop and remove running containers for all MicroBank services.

printf "\nStopping containers...\n\n"
# shellcheck disable=SC2046
docker container stop $(docker container ls -q --filter name=microbank-*)
printf "\nRemoving containers...\n\n"
# shellcheck disable=SC2046
docker container rm $(docker container ls -a -q --filter name=microbank-*)