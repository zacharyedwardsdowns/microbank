# Pull docker images for all MicroBank services.
cd "$(dirname "$0")"
cd ../angular; ./pull.sh
cd ../eureka; ./pull.sh
cd ../zuul; ./pull.sh
cd ../customer; ./pull.sh