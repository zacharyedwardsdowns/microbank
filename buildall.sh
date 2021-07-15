# Build all MicroBank utility services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

cd utility-services || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
sh buildall.sh

echo -e "\nBuilding Angular Service"
cd ../angular || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
ng build

echo -e "\nBuilding Customer Service"
cd ../customer || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain clean build