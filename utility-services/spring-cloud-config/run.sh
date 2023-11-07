#!/bin/bash
docker run -d -p "8888:8888" --name "microbank-spring-cloud-config" --network "microbank-network" zacharyed/microbank-spring-cloud-config