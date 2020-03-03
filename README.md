# MicroBank
A microservice banking application.

---

### Docker-Compose
The recommended way to run MicroBank.

Run Application:<br>`docker-compose up`

Kill Application:<br>`docker-compose down`

Reset Containers:<br>`docker-compose rm`

---

### Docker
Setup is required to run MicroBank on Docker.

Replace localhost with `172.35.6.72` in defaultZone of the application.yaml for all services that register with Eureka.

Start the application using:<br>`./runall.sh`

---

### Scripts
Scripts to automate development tasks.

Executes 'mvn clean package' for all services:<br>`./packageall.sh`

Executes 'mvn clean' for all services:<br>`./cleanall.sh`

Executes 'docker build' for all services:<br>`./docker/buildall.sh`

Executes 'docker run' for all services:<br>`./docker/runall.sh`

Executes 'docker stop' and 'docker rm' for all services:<br>`./docker/stopall.sh`

Executes 'docker push' for all services:<br>`./docker/pushall.sh`

Formats all java files using prettier-java:<br>`./prettier-java.sh`