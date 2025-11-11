package main.kotlin.cli

import factory.LexerFactoryRegistry
import factory.ParserFactoryRegistry
import main.kotlin.analyzer.ConfigLoader
import main.kotlin.analyzer.DefaultAnalyzer
import org.example.InterpreterFactory
import org.example.formatter.Formatter
import org.example.input.StdInput
import org.example.iterator.ListIterator
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import java.io.File

/**
 * Main CLI orchestrator that handles all PrintScript operations
 */
class PrintScriptCLI {
    private val analyzer = DefaultAnalyzer()

    /**
     * Processes a PrintScript file through the complete pipeline
     */
    fun processFile(
        sourceFile: File,
        version: String = "1.0",
        onProgress: (String) -> Unit = {},
    ): ProcessingResult {
        try {
            onProgress("Reading source file...")
            val sourceCode = sourceFile.readText(Charsets.UTF_8)
            onProgress("File read successfully (${sourceCode.length} characters)")

            onProgress("Performing lexical analysis...")
            val lexerFactory = LexerFactoryRegistry.getFactory(version)
            val lexer = lexerFactory.create()
            val tokens = lexer.tokenize(sourceCode)
            onProgress("Lexical analysis completed (${tokens.size} tokens found)")

            onProgress("Performing syntax analysis...")
            val parserFactory = ParserFactoryRegistry.getFactory(version)
            val parser = parserFactory.create()
            val ast = parser.parse(tokens)
            onProgress("Syntax analysis completed (${ast.size} statements parsed)")

            return ProcessingResult.Success(
                sourceCode = sourceCode,
                tokens = tokens,
                ast = ast,
                file = sourceFile,
                version = version,
            )
        } catch (e: Exception) {
            return ProcessingResult.Error(
                error = e,
                file = sourceFile,
                version = version,
            )
        }
    }

    /**
     * Validates a PrintScript file (syntax and semantics)
     */
    fun validate(
        sourceFile: File,
        version: String = "1.0",
        onProgress: (String) -> Unit = {},
    ): ValidationResult {
        val result = processFile(sourceFile, version, onProgress)

        return when (result) {
            is ProcessingResult.Success -> {
                onProgress("Validation completed successfully!")
                ValidationResult.Success(
                    file = result.file,
                    version = result.version,
                    tokenCount = result.tokens.size,
                    statementCount = result.ast.size,
                )
            }
            is ProcessingResult.Error -> {
                onProgress("Validation failed!")
                ValidationResult.Error(
                    error = result.error,
                    file = result.file,
                    version = result.version,
                )
            }
        }
    }

    /**
     * Executes a PrintScript file
     */
    fun execute(
        sourceFile: File,
        version: String = "1.0",
        onProgress: (String) -> Unit = {},
    ): ExecutionResult {
        val result = processFile(sourceFile, version, onProgress)

        return when (result) {
            is ProcessingResult.Success -> {
                onProgress("⚡ Executing program...")

                val output =
                    object : Output {
                        override fun write(msg: String) {
                            kotlin.io.print(msg)
                        }
                    }

                val interpreter =
                    when (version) {
                        "1.1" -> InterpreterFactory.createInterpreterVersion11(output, StdInput)
                        else -> InterpreterFactory.createInterpreterVersion10(output, StdInput)
                    }

                // Execute all statements using iterator
                val nodeIterator = ListIterator(result.ast)
                interpreter.interpret(nodeIterator)

                onProgress("Execution completed successfully!")
                ExecutionResult.Success(
                    file = result.file,
                    version = result.version,
                    tokenCount = result.tokens.size,
                    statementCount = result.ast.size,
                )
            }
            is ProcessingResult.Error -> {
                onProgress("Execution failed!")
                ExecutionResult.Error(
                    error = result.error,
                    file = result.file,
                    version = result.version,
                )
            }
        }
    }

    /**
     * Formats a PrintScript file according to configuration
     */
    fun format(
        sourceFile: File,
        configFile: File? = null,
        outputFile: File? = null,
        version: String = "1.0",
        onProgress: (String) -> Unit = {},
    ): FormattingResult {
        val result = processFile(sourceFile, version, onProgress)

        return when (result) {
            is ProcessingResult.Success -> {
                onProgress("Applying formatting rules...")

                val configFileToUse = configFile ?: File("formatter-config.json")

                if (!configFileToUse.exists()) {
                    onProgress("Configuration file not found, using default rules...")
                    createDefaultFormatterConfig(configFileToUse)
                }

                val formattedCode = Formatter.formatMultiple(result.ast, configFileToUse)
                onProgress("Formatting completed")

                if (outputFile != null) {
                    onProgress("Writing formatted code to: ${outputFile.absolutePath}")
                    outputFile.writeText(formattedCode)
                    onProgress("File written successfully")
                }

                FormattingResult.Success(
                    file = result.file,
                    version = result.version,
                    tokenCount = result.tokens.size,
                    statementCount = result.ast.size,
                    formattedCode = formattedCode,
                    configFile = configFileToUse,
                    outputFile = outputFile,
                )
            }
            is ProcessingResult.Error -> {
                onProgress("Formatting failed!")
                FormattingResult.Error(
                    error = result.error,
                    file = result.file,
                    version = result.version,
                )
            }
        }
    }

    /**
     * Analyzes a PrintScript file for code quality and style issues
     */
    fun analyze(
        sourceFile: File,
        configFile: File? = null,
        version: String = "1.0",
        onProgress: (String) -> Unit = {},
    ): AnalysisResult {
        val result = processFile(sourceFile, version, onProgress)

        return when (result) {
            is ProcessingResult.Success -> {
                onProgress("Performing static analysis...")

                val configFileToUse = configFile ?: File("analyzer-config.json")

                if (!configFileToUse.exists()) {
                    onProgress("Configuration file not found, using default rules...")
                    createDefaultAnalyzerConfig(configFileToUse)
                }

                val config = ConfigLoader.loadFromJson(configFileToUse)
                val analysisResult = analyzer.analyze(result.ast, config)
                onProgress("Static analysis completed")

                AnalysisResult.Success(
                    file = result.file,
                    version = result.version,
                    tokenCount = result.tokens.size,
                    statementCount = result.ast.size,
                    analysisResult = analysisResult,
                    configFile = configFileToUse,
                )
            }
            is ProcessingResult.Error -> {
                onProgress("Analysis failed!")
                AnalysisResult.Error(
                    error = result.error,
                    file = result.file,
                    version = result.version,
                )
            }
        }
    }

    private fun createDefaultFormatterConfig(configFile: File) {
        configFile.writeText(
            """
            {
              "lineBreaksBeforePrints": 1,
              "spaceAroundEquals": true,
              "spaceBeforeColon": false,
              "spaceAfterColon": false,
              "spaceAroundAssignment": true
            }
            """.trimIndent(),
        )
    }

    private fun createDefaultAnalyzerConfig(configFile: File) {
        configFile.writeText(
            """
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
            """.trimIndent(),
        )
    }
}
