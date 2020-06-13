# Clean all maven target folders for all MicroBank services.

cd angular; rm -r dist
cd ../eureka; gradle clean
cd ../zuul; gradle clean
cd ../customer; gradle clean