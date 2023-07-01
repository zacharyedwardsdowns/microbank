if [ -z "$1" ]
then
  docker run -d -p "8761:8761" --name "microbank-eureka" --network "microbank-network" zacharyed/microbank-eureka
  printf "Started eureka with a random IP.\n"
else
  docker run -d -p "8761:8761" --name "microbank-eureka" --network "microbank-network" --ip "$1" zacharyed/microbank-eureka
fi