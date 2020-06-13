# Clean build directories for all MicroBank services.
cd "$(dirname "$0")"
cd angular; rm -r dist
cd ../eureka; gradle clean
cd ../zuul; gradle clean
cd ../customer; gradle clean