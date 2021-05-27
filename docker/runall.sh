# Run docker files for all MicroBank services.
cd "$(dirname "$0")"

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
  echo "Creating the microbank-network..."
  docker network create microbank-network --subnet "$networkSubnet"
  echo ""
fi



# Begin running services.
echo "Starting spring-cloud-config..."
cd ../spring-cloud-config; ./run.sh

echo "Starting eureka..."
cd ../eureka; ./run.sh "$eurekaIp"

echo ""
echo "Starting angular..."
cd ../angular; ./run.sh

echo ""
echo "Starting zuul..."
cd ../zuul; ./run.sh

echo ""
echo "Starting customer..."
cd ../customer; ./run.sh

echo ""
echo "All containers started."