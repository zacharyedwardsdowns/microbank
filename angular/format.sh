# npm -g install prettier js-beautify
cd "$(dirname "$0")"
prettier --write "**/*.ts"
prettier --write "**/*.js"
js-beautify --indent-size 2 "**/*.html"