# Clean all maven target folders for all MicroBank services.

cd angular; rm -r dist
cd ../eureka; mvn clean
cd ../zuul; mvn clean
cd ../registration; mvn clean