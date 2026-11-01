#!/usr/bin/env bash
# Starts the application with persistent storage.
# Override the data file with:  LIBRARY_DATA_FILE=/path/books.csv ./scripts/run.sh
set -euo pipefail
cd "$(dirname "$0")/.."

JAR="${JAR:-target/library-inventory-cli.jar}"
[ -f "$JAR" ] || JAR="library-inventory-cli.jar"     # inside a release bundle
[ -f "$JAR" ] || { echo "JAR not found. Run scripts/build.sh first." >&2; exit 1; }

export LIBRARY_DATA_FILE="${LIBRARY_DATA_FILE:-data/books.csv}"
exec java ${JAVA_OPTS:-} -jar "$JAR" "$@"
