#!/usr/bin/env bash
# Builds the executable JAR. Uses Maven (runs the tests) when available,
# (or when SKIP_MAVEN=1) falls back to plain javac/jar so the app can still be built on a bare JDK.
set -euo pipefail
cd "$(dirname "$0")/.."

if [ "${SKIP_MAVEN:-0}" != "1" ] && command -v mvn >/dev/null 2>&1; then
  echo "[build] Maven found - running tests and packaging"
  mvn -B clean package
else
  echo "[build] Maven skipped or not found - compiling with javac (tests are skipped)"
  rm -rf target/classes && mkdir -p target/classes
  javac -d target/classes src/main/java/com/library/*.java
  printf 'Main-Class: com.library.LibraryApp\n' > target/MANIFEST.MF
  jar cfm target/library-inventory-cli.jar target/MANIFEST.MF -C target/classes .
fi
echo "[build] Done: target/library-inventory-cli.jar"
