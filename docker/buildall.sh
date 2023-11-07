#!/bin/bash
# Build docker images for all MicroBank services.
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
  sh ../utility-services/docker/buildall.sh --multi
  printf "\nDocker: Building Angular Service\n"
  sh ../angular/build.sh --multi
  printf "\nDocker: Building Customer Service\n"
  sh ../customer/build.sh --multi
else
  sh ../utility-services/docker/buildall.sh
  printf "\nDocker: Building Angular Service\n"
  sh ../angular/build.sh
  printf "\nDocker: Building Customer Service\n"
  sh ../customer/build.sh
fi