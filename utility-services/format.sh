# Format all MicroBank utility java files using google java format.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

echo -e "\nFormatting Spring Cloud Config Service"
cd spring-cloud-config || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle googleJavaFormat

echo -e "\nFormatting Eureka Service"
cd ../eureka || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle googleJavaFormat

echo -e "\nFormatting Zuul Service"
cd ../zuul || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle googleJavaFormat