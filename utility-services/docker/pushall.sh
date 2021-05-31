# Push docker images for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nDocker: Pushing Spring Cloud Config Service"
sh ../spring-cloud-config/push.sh
echo -e "\nDocker: Pushing Eureka Service"
sh ../eureka/push.sh
echo -e "\nDocker: Pushing Zuul Service"
sh ../zuul/push.sh
