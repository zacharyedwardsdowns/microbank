# npm -g install prettier js-beautify
cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

prettier --write "**/*.ts"
prettier --write "**/*.js"
js-beautify --indent-size 2 "**/*.html"
