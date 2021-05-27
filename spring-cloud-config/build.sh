# Used to keep the sensitive configs out of the repository.
. ../../microbank-config/configServerEnv.sh

gradle clean build; gradle_exit_code="$?"

if [[ ( "$gradle_exit_code" == 0 ) ]]; then
	echo ""
	docker build --build-arg URI="${URI}" --build-arg PASS="${PASS}" -t registry.gitlab.com/zacharyedwardsdowns/micro-bank/spring-cloud-config:latest .
	docker rmi "$(docker images -f "dangling=true" -q)" &> /dev/null
fi