#!/bin/bash
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

# Used to keep the sensitive configs out of the repository.
. ../../../microbank-config/scripts/configServerEnv.sh

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
    docker build --build-arg URI="${URI}" --build-arg PASS="${PASS}" -t zacharyed/microbank-spring-cloud-config -f Dockerfile.multi .
    # shellcheck disable=SC2046
    docker rmi $(docker images -f "dangling=true" -q) &>/dev/null
else
  gradle clean build; gradle_exit_code="$?"

  if [ "$gradle_exit_code" -eq 0 ]; then
    printf "\n"
    docker build --build-arg URI="${URI}" --build-arg PASS="${PASS}" -t zacharyed/microbank-spring-cloud-config .
    # shellcheck disable=SC2046
    docker rmi $(docker images -f "dangling=true" -q) &>/dev/null
  fi
fi