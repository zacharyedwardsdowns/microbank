# Build all MicroBank utility services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nBuilding Spring Cloud Config Service"
cd spring-cloud-config || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain clean build

echo -e "\nBuilding Eureka Service"
cd ../eureka || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain clean build

echo -e "\nBuilding Zuul Service"
cd ../zuul || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain clean build
