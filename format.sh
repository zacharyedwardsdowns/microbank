# Format all microbank java files using google java format.
#
# Put in gradle.properties to allow formatting with Java 17
# org.gradle.jvmargs=--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
#   --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED

cd "$(dirname "$0")" || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)

cd utility-services || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
sh format.sh

echo -e "\nFormatting Customer Service"
cd ../customer || (echo -e "\nFailed to change directory at [$0: $LINENO]"; exit 1)
gradle --console=plain spotlessJavaApply