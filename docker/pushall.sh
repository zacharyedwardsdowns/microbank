# Push docker images for all MicroBank services.
cd "$(dirname "$0")"
cd ../angular; ./push.sh
cd ../eureka; ./push.sh
cd ../zuul; ./push.sh
cd ../customer; ./push.sh