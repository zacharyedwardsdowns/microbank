# Maven package all MicroBank services.

cd angular; ng build
cd ../eureka; mvn clean package
cd ../zuul; mvn clean package
cd ../customer; mvn clean package