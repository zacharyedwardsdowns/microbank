# MicroBank
A microservice banking application.

---

### Docker Compose
The recommended way to run MicroBank.

Run Application:<br>`docker-compose up`

Stop Application:<br>`docker-compose down`

---

### Docker
Setup is required to run MicroBank on Docker without Docker Compose.

Replace localhost with `172.35.6.72` in defaultZone of the application.yaml for all services that register with Eureka.

Start the application using:<br>`sh runall.sh`

---

### Scripts
Scripts to automate development tasks.

Executes 'gradle clean build' for all services:<br>`sh buildall.sh`

Executes 'gradle clean' for all services:<br>`sh cleanall.sh`

Executes 'docker build' for all services:<br>`sh docker/buildall.sh`

Executes 'docker run' for all services:<br>`sh docker/runall.sh`

Executes 'docker stop' and 'docker rm' for all services:<br>`sh docker/stopall.sh`

Executes 'docker push' for all services:<br>`sh docker/pushall.sh`

Formats all java files using google-java-format:<br>`sh format.sh`