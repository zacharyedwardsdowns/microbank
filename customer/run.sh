#!/bin/bash
docker run -d -p "6010:6010" --restart on-failure:3 --name "microbank-customer" --network "microbank-network" zacharyed/microbank-customer