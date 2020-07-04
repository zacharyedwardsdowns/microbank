ng build; ng_exit_code="$?"

if [[( "$ng_exit_code" == 0 )]]; then
  echo ""
  docker build -t registry.gitlab.com/zacharyedwardsdowns/micro-bank/angular:latest .
  docker rmi $(docker images -f "dangling=true" -q) &> /dev/null
fi