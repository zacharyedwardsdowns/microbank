# Build docker images for all MicroBank services.
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
  sh ../utility-services/docker/buildall.sh --multi
  echo -e "\nDocker: Building Angular Service"
  sh ../angular/build.sh --multi
  echo -e "\nDocker: Building Customer Service"
  sh ../customer/build.sh --multi
else
  sh ../utility-services/docker/buildall.sh
  echo -e "\nDocker: Building Angular Service"
  sh ../angular/build.sh
  echo -e "\nDocker: Building Customer Service"
  sh ../customer/build.sh
fi