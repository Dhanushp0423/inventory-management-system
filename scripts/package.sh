#!/usr/bin/env bash
# Builds the JAR and assembles a self-contained release bundle in dist/:
#   library-inventory-<version>.zip  ->  JAR + run scripts + README + docs
# This is what an operations team receives for deployment.
set -euo pipefail
cd "$(dirname "$0")/.."

./scripts/build.sh
VERSION="$(sed -n 's:.*<version>\(.*\)</version>.*:\1:p' pom.xml | head -1)"
NAME="library-inventory-${VERSION}"
STAGE="dist/${NAME}"

rm -rf dist && mkdir -p "${STAGE}/scripts" "${STAGE}/data"
cp target/library-inventory-cli.jar "${STAGE}/"
cp scripts/run.sh scripts/run.bat "${STAGE}/scripts/"
chmod +x "${STAGE}/scripts/run.sh"
cp README.md "${STAGE}/"
cp -r docs "${STAGE}/docs"
touch "${STAGE}/data/.keep"

( cd dist && zip -qr "${NAME}.zip" "${NAME}" )
echo "[package] Release bundle: dist/${NAME}.zip"
