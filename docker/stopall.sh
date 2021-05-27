# Stop and remove running containers for all MicroBank services.

echo "Stopping containers..."
docker container stop "$(docker container ls -q --filter name=microbank-*)"
echo "Removing containers..."
docker container rm "$(docker container ls -a -q --filter name=microbank-*)"