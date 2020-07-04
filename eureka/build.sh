gradle clean build; gradle_exit_code="$?"

if [[( "$gradle_exit_code" == 0 )]]; then
	echo ""
	docker build -t registry.gitlab.com/zacharyedwardsdowns/micro-bank/eureka:latest .
	docker rmi $(docker images -f "dangling=true" -q) &> /dev/null
fi