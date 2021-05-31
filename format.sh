# Format all microbank java files using google java format.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

cd utility-services || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
sh format.sh

echo -e "\nFormatting Customer Service"
cd ../customer || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle googleJavaFormat