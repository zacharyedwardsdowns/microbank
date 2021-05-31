if [ -z "$1" ]
then
  docker run -d -p "8761:8761" --name "microbank-eureka" --network "microbank-network" registry.gitlab.com/zacharyedwardsdowns/micro-bank/eureka
  echo "Started eureka with a random IP."
else
  docker run -d -p "8761:8761" --name "microbank-eureka" --network "microbank-network" --ip "$1" registry.gitlab.com/zacharyedwardsdowns/micro-bank/eureka
fi