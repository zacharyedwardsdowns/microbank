# Build docker images for all MicroBank services.

cd angular; ./build.sh
cd ../eureka; ./build.sh
cd ../zuul; ./build.sh
cd ../registration; ./build.sh