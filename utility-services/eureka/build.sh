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
    docker build -t zacharyed/microbank-eureka -f Dockerfile.multi .
    # shellcheck disable=SC2046
    docker rmi $(docker images -f "dangling=true" -q) &>/dev/null
else
  gradle --console=plain clean build; gradle_exit_code="$?"

  if [[ ("$gradle_exit_code" == 0) ]]; then
    printf "\n"
    docker build -t zacharyed/microbank-eureka .
    # shellcheck disable=SC2046
    docker rmi $(docker images -f "dangling=true" -q) &>/dev/null
  fi
fi