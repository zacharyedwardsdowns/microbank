#!/bin/bash
docker run -d -p "8765:8765" --name "microbank-spring-cloud-gateway" --network "microbank-network" zacharyed/microbank-spring-cloud-gateway