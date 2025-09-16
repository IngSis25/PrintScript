# PrintScript CLI

A command-line interface for the PrintScript language processor that supports validation, execution, formatting, and static analysis.

## Architecture

The CLI is built with a clean architecture pattern:

- **`PrintScriptCLI`**: Main orchestrator class that handles all operations
- **Command Classes**: Individual CLIkt commands for each operation
- **Result Classes**: Sealed classes for type-safe result handling
- **Progress Callbacks**: Real-time progress reporting during operations

## Features

- **Validation**: Check syntax and semantics of PrintScript files
- **Execution**: Run PrintScript programs
- **Formatting**: Format code according to configuration rules
- **Analyzing**: Perform static analysis for code quality and style

## Usage

### Basic Commands

```bash
# Validate a PrintScript file
./gradlew :cli:run --args="validation example.ps"

# Execute a PrintScript file
./gradlew :cli:run --args="execution example.ps"

# Format a PrintScript file
./gradlew :cli:run --args="formatting example.ps --config formatter-config.json"

# Analyze a PrintScript file
./gradlew :cli:run --args="analyzing example.ps --config analyzer-config.json"
```

### Command Options

#### Validation Command
```bash
printscript validation <SOURCE_FILE> [--version VERSION]
```

- `SOURCE_FILE`: Path to the PrintScript source file to validate
- `--version, -v`: Version of PrintScript to use (default: 1.0)

#### Execution Command
```bash
printscript execution <SOURCE_FILE> [--version VERSION]
```

- `SOURCE_FILE`: Path to the PrintScript source file to execute
- `--version, -v`: Version of PrintScript to use (default: 1.0)

#### Formatting Command
```bash
printscript formatting <SOURCE_FILE> [--config CONFIG_FILE] [--output OUTPUT_FILE] [--version VERSION]
```

- `SOURCE_FILE`: Path to the PrintScript source file to format
- `--config, -c`: Path to the formatting configuration file (JSON format)
- `--output, -o`: Path to write the formatted output (default: stdout)
- `--version, -v`: Version of PrintScript to use (default: 1.0)

#### Analyzing Command
```bash
printscript analyzing <SOURCE_FILE> [--config CONFIG_FILE] [--version VERSION]
```

- `SOURCE_FILE`: Path to the PrintScript source file to analyze
- `--config, -c`: Path to the analyzer configuration file (JSON format)
- `--version, -v`: Version of PrintScript to use (default: 1.0)

## Configuration Files

### Formatter Configuration (formatter-config.json)
```json
{
  "lineBreaksBeforePrints": true,
  "spaceAroundColons": true,
  "spaceAroundEquals": true
}
```

### Analyzer Configuration (analyzer-config.json)
```json
{
  "identifierFormat": {
    "enabled": true,
    "format": "CAMEL_CASE"
  },
  "printlnRestrictions": {
    "enabled": true,
    "allowOnlyIdentifiersAndLiterals": true
  },
  "maxErrors": 10,
  "enableWarnings": true,
  "strictMode": false
}
```

## Error Reporting

The CLI provides detailed error reporting with:
- Error messages with clear descriptions
- File position information (line and column)
- Suggestions for fixing issues
- Progress indicators during processing

## Examples

### Example PrintScript File (example.ps)
```printscript
let name: string = "PrintScript"
let age: number = 25
let isStudent: boolean = true

println("Hello, " + name + "!")
println("Age: " + age)
println("Student: " + isStudent)

let x: number = 10
let y: number = 20
let sum: number = x + y

println("Sum: " + sum)
```

### Running Examples
```bash
# Validate the example file
./gradlew :cli:run --args="validation example.ps"

# Execute the example file
./gradlew :cli:run --args="execution example.ps"

# Format the example file
./gradlew :cli:run --args="formatting example.ps --output formatted.ps"

# Analyze the example file
./gradlew :cli:run --args="analyzing example.ps"
```

## Building

To build the CLI module:

```bash
./gradlew :cli:build
```

To run the CLI:

```bash
./gradlew :cli:run --args="<command> <arguments>"
```

## Dependencies

- **CLIkt**: Command-line interface framework
- **PrintScript Modules**: lexer, parser, interpreter, formatter, analyzer, common
