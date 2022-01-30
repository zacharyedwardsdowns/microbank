# Pull docker images for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nDocker: Pulling Spring Cloud Config Service"
sh ../spring-cloud-config/pull.sh
echo -e "\nDocker: Pulling Eureka Service"
sh ../eureka/pull.sh
echo -e "\nDocker: Pulling Spring Cloud Gateway Service"
sh ../spring-cloud-gateway/pull.sh
