# Format all microbank java files using google java format.
cd "$(dirname "$0")"
echo -e "Formatting Eureka Service"
cd eureka; gradle googleJavaFormat
echo -e "\nFormatting Zuul Service"
cd ../zuul; gradle googleJavaFormat
echo -e "\nFormatting Customer Service"
cd ../customer; gradle googleJavaFormat