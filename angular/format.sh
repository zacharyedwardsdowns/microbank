cd "$(dirname "$0")"
prettier --write "**/*.ts"
prettier --write "**/*.js"
js-beautify --indent-size 2 "**/*.html"