# Clean build directories for all MicroBank utility services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nCleaning Spring Cloud Config Service"
cd spring-cloud-config || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle clean

echo -e "\nCleaning Eureka Service"
cd ../eureka || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle clean

echo -e "\nCleaning Zuul Service"
cd ../zuul || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle clean