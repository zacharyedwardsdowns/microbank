# MicroBank

A microservice banking application.

---

### Docker Compose

The recommended way to run MicroBank.

Run Application:<br>`docker-compose up`

Stop Application:<br>`docker-compose down`

---

### Docker

Run Application:<br>`sh docker/runall.sh`

Stop Application:<br>`sh docker/stopall.sh`

---

### Scripts

Scripts to automate development tasks.

Executes 'gradle clean build' for all services:<br>`sh buildall.sh`

Executes 'gradle clean' for all services:<br>`sh cleanall.sh`

Executes 'docker build' for all services:<br>`sh docker/buildall.sh`

Docker multi-stage builds:<br>`sh docker/buildall.sh --multi`

Executes 'docker run' for all services:<br>`sh docker/runall.sh`

Executes 'docker stop' and 'docker rm' for all services:<br>`sh docker/stopall.sh`

Executes 'docker pull' for all services:<br>`sh docker/pullall.sh`

Executes 'docker push' for all services:<br>`sh docker/pushall.sh`

Formats all java files using google-java-format:<br>`sh format.sh`

---

### Postman Workspace

Postman workspace containing the requests for this project:<br>`https://www.postman.com/zacharyedwardsdowns/workspace/microbank/overview`
