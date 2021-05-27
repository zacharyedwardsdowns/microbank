# Build docker images for all MicroBank services.
cd "$(dirname "$0")"
cd ../angular; ./build.sh
cd ../eureka; ./build.sh
cd ../zuul; ./build.sh
cd ../customer; ./build.sh
cd ../spring-cloud-config; ./build.sh