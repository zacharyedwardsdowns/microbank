cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

# Used to keep the sensitive configs out of the repository.
. ../../../microbank-config/configServerEnv.sh

gradle --console=plain clean build; gradle_exit_code="$?"

if [[ ( "$gradle_exit_code" == 0 ) ]]; then
	echo ""
	docker build --build-arg URI="${URI}" --build-arg PASS="${PASS}" -t registry.gitlab.com/zacharyedwardsdowns/micro-bank/spring-cloud-config:latest .
	# shellcheck disable=SC2046
	docker rmi $(docker images -f "dangling=true" -q) &> /dev/null
fi