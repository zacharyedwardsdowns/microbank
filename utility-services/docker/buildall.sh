# Build docker images for all MicroBank utility services.
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

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
  printf "\nDocker: Building Spring Cloud Config Service\n"
  sh ../spring-cloud-config/build.sh --multi
  printf "\nDocker: Building Eureka Service\n"
  sh ../eureka/build.sh --multi
  printf "\nDocker: Building Spring Cloud Gateway Service\n"
  sh ../spring-cloud-gateway/build.sh --multi
else
  printf "\nDocker: Building Spring Cloud Config Service\n"
  sh ../spring-cloud-config/build.sh
  printf "\nDocker: Building Eureka Service\n"
  sh ../eureka/build.sh
  printf "\nDocker: Building Spring Cloud Gateway Service\n"
  sh ../spring-cloud-gateway/build.sh
fi
