#!/usr/bin/env sh
set -e
DIRNAME=$(cd "$(dirname "$0")" && pwd)
"$DIRNAME/gradle/wrapper/gradlew.sh" "$@"
