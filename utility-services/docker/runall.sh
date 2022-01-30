# Run docker files for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

# Check if a network exists, if not create one.
networkSubnet="172.35.6.0/16"
eurekaIp="172.35.6.72"
networkFound="false"

for network in $(docker network ls --format "{{.Name}}")
do
  if [ "$network" = "microbank-network" ]
  then
    networkFound="true"
    break
  fi
done

if [ "$networkFound" = "false" ]
then
  echo -e "\nCreating the microbank-network..."
  docker network create microbank-network --subnet "$networkSubnet"
fi



# Begin running services.
echo -e "\nStarting spring-cloud-config..."
sh ../spring-cloud-config/run.sh

echo -e "\nStarting eureka..."
sh ../eureka/run.sh "$eurekaIp"

echo -e "\nStarting spring-cloud-gateway..."
sh ../spring-cloud-gateway/run.sh

echo -e "\nAll utility containers started.\n"