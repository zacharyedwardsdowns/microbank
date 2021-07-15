# Clean build directories for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

cd utility-services || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
sh cleanall.sh

echo -e "\nCleaning Angular Service"
cd ../angular || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
if [ -d "dist" ]; then
  rm -r dist
fi
echo -e "\n- DIST DIRECTORY REMOVED -"

echo -e "\nCleaning Customer Service"
cd ../customer || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain clean