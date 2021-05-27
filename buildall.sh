# Build all MicroBank services.
cd "$(dirname "$0")"
cd angular; ng build
cd ../eureka; gradle clean build
cd ../zuul; gradle clean build
cd ../customer; gradle clean build
cd ../spring-cloud-config; gradle clean build