#!/usr/bin/env bash
set -euo pipefail

# DIR = carpeta donde está este script (resources)
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ROOT = raíz del repo (intenta con Git; si no hay Git, sube tres niveles)
if ROOT="$(git -C "$DIR" rev-parse --show-toplevel 2>/dev/null)"; then
  :
else
  ROOT="$(cd "$DIR/../../.." && pwd)"
fi

GRADLEW="$ROOT/gradlew"

if [[ ! -x "$GRADLEW" ]]; then
  echo "Gradle wrapper no encontrado o sin permisos en: $GRADLEW" >&2
  echo "Solución: ubicáte en la raíz del repo y corré: chmod +x gradlew" >&2
  exit 1
fi

# Ejemplos de uso del CLI (con versión seleccionable)
# Ajustá paths de archivos de prueba según tu repo

echo "1. Testing Execute con versión 1.0..."
"$GRADLEW" :cli:run --args="execute path/to/file.ps --version 1.0"

echo "2. Testing Execute con versión 1.1 (default)..."
"$GRADLEW" :cli:run --args="execute path/to/file.ps"

echo "3. Testing Validate con versión 1.0..."
"$GRADLEW" :cli:run --args="validate path/to/file.ps --version 1.0"

echo "4. Testing Validate con versión 1.1..."
"$GRADLEW" :cli:run --args="validate path/to/file.ps"

echo "5. Testing Analyze con versión 1.0..."
"$GRADLEW" :cli:run --args="analyze path/to/file.ps path/to/rules.json --version 1.0"

echo "6. Testing Analyze con versión 1.1..."
"$GRADLEW" :cli:run --args="analyze path/to/file.ps path/to/rules.json"

echo "7. Testing Format con versión 1.0..."
"$GRADLEW" :cli:run --args="format path/to/file.ps path/to/rules.json --version 1.0"

echo "8. Testing Format con versión 1.1..."
"$GRADLEW" :cli:run --args="format path/to/file.ps path/to/rules.json"
