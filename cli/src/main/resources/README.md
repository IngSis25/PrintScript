# Archivos de Ejemplo para CLI

Este directorio contiene archivos de ejemplo para probar el CLI de PrintScript.

## Archivos PrintScript

- `example-v10.ps`: Ejemplo básico para versión 1.0 (sin if/else)
- `example-v11.ps`: Ejemplo con if/else para versión 1.1
- `example-simple-v10.ps`: Ejemplo simple para versión 1.0
- `example-simple-v11.ps`: Ejemplo simple para versión 1.1

## Archivos de Configuración

- `analyzer-rules.json`: Reglas para el comando `analyze`
- `formatter-rules.json`: Reglas para el comando `format`

## Ejemplos de Uso

### Execute
```bash
# Versión por defecto (1.1)
./gradlew :cli:run --args="execute src/main/resources/example-v11.ps"

# Versión 1.0
./gradlew :cli:run --args="execute src/main/resources/example-v10.ps --version 1.0"

# Versión 1.1 explícita
./gradlew :cli:run --args="execute src/main/resources/example-v11.ps --version 1.1"
```

### Validate
```bash
# Validar con versión 1.0
./gradlew :cli:run --args="validate src/main/resources/example-v10.ps --version 1.0"

# Validar con versión 1.1
./gradlew :cli:run --args="validate src/main/resources/example-v11.ps --version 1.1"
```

### Analyze
```bash
# Analizar con versión 1.0
./gradlew :cli:run --args="analyze src/main/resources/example-v10.ps src/main/resources/analyzer-rules.json --version 1.0"

# Analizar con versión 1.1
./gradlew :cli:run --args="analyze src/main/resources/example-v11.ps src/main/resources/analyzer-rules.json --version 1.1"
```

### Format
```bash
# Formatear con versión 1.0
./gradlew :cli:run --args="format src/main/resources/example-v10.ps src/main/resources/formatter-rules.json --version 1.0"

# Formatear con versión 1.1
./gradlew :cli:run --args="format src/main/resources/example-v11.ps src/main/resources/formatter-rules.json --version 1.1"
```

## Script de Prueba

Se incluye un script `test-cli.sh` que ejecuta todas las pruebas automáticamente:

```bash
cd cli/src/main/kotlin/resources
./test-cli.sh
```

O desde la raíz del proyecto:

```bash
./cli/src/main/kotlin/resources/test-cli.sh
```

## Notas

- La versión por defecto es 1.1 si no se especifica `--version` o `-v`
- Los archivos `example-v10.ps` no deben contener estructuras `if/else` ya que solo están disponibles en versión 1.1
- Los archivos `example-v11.ps` pueden usar todas las características incluyendo `if/else`
- El script de prueba crea archivos temporales para las pruebas de formateo y los elimina automáticamente

