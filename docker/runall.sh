# Run docker files for all MicroBank services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

# Begin running services.
sh ../utility-services/docker/runall.sh

echo -e "\nStarting customer..."
sh ../customer/run.sh

echo -e "\nStarting angular..."
sh ../angular/run.sh

echo -e "\nAll containers started.\n"