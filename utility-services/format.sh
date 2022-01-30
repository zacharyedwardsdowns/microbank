# Format all MicroBank utility java files using google java format.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nFormatting Spring Cloud Config Service"
cd spring-cloud-config || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain spotlessJavaApply

echo -e "\nFormatting Eureka Service"
cd ../eureka || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain spotlessJavaApply

echo -e "\nFormatting Spring Cloud Gateway Service"
cd ../spring-cloud-gateway || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain spotlessJavaApply