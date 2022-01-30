# Build docker images for all MicroBank utility services.
cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

has_param() {
  local term="$1"
  shift
  for arg; do
    if [[ $arg == "$term" ]]; then
      return 0
    fi
  done
  return 1
}

if has_param '--multi' "$@"; then
  echo -e "\nDocker: Building Spring Cloud Config Service"
  sh ../spring-cloud-config/build.sh --multi
  echo -e "\nDocker: Building Eureka Service"
  sh ../eureka/build.sh --multi
  echo -e "\nDocker: Building Spring Cloud Gateway Service"
  sh ../spring-cloud-gateway/build.sh --multi
else
  echo -e "\nDocker: Building Spring Cloud Config Service"
  sh ../spring-cloud-config/build.sh
  echo -e "\nDocker: Building Eureka Service"
  sh ../eureka/build.sh
  echo -e "\nDocker: Building Spring Cloud Gateway Service"
  sh ../spring-cloud-gateway/build.sh
fi
