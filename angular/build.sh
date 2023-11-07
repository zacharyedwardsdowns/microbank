#!/bin/bash
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
  printf "\n"
  docker build -t zacharyed/microbank-angular -f Dockerfile.multi .
  # shellcheck disable=SC2046
  docker rmi $(docker images -f "dangling=true" -q) &> /dev/null
else
  ng build; ng_exit_code="$?"

  if [ "$ng_exit_code" -eq 0 ]; then
    printf "\n"
    docker build -t zacharyed/microbank-angular .
    # shellcheck disable=SC2046
    docker rmi $(docker images -f "dangling=true" -q) &> /dev/null
  fi
fi
