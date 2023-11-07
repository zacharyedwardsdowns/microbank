#!/bin/bash
# Format all microbank java files using google java format.
#
# Put in gradle.properties to allow formatting with Java 17
# org.gradle.jvmargs=--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED

cd "$(dirname "$0")" || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)

cd utility-services || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
sh format.sh

printf "\nFormatting Customer Service\n"
cd ../customer || (printf "\nFailed to change directory at [$0: $LINENO]\n"; exit 1)
gradle spotlessJavaApply