# Build docker images for all MicroBank utility services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nDocker: Building Spring Cloud Config Service"
sh ../spring-cloud-config/build.sh
echo -e "\nDocker: Building Eureka Service"
sh ../eureka/build.sh
echo -e "\nDocker: Building Zuul Service"
sh ../zuul/build.sh
