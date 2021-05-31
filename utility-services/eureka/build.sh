cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

gradle clean build; gradle_exit_code="$?"

if [[ ( "$gradle_exit_code" == 0 ) ]]; then
	echo ""
	docker build -t registry.gitlab.com/zacharyedwardsdowns/micro-bank/eureka:latest .
	# shellcheck disable=SC2046
	docker rmi $(docker images -f "dangling=true" -q) &> /dev/null
fi