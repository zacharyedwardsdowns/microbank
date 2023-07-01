# Build all MicroBank utility services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

printf "\nBuilding Spring Cloud Config Service\n"
cd spring-cloud-config || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle --console=plain clean build

printf "\nBuilding Eureka Service\n"
cd ../eureka || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle --console=plain clean build

printf "\nBuilding Spring Cloud Gateway Service\n"
cd ../spring-cloud-gateway || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle --console=plain clean build
