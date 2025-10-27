package runner

import factory.LexerFactoryRegistry
import factory.ParserFactoryRegistry
import kotlinx.serialization.json.JsonObject
import main.kotlin.analyzer.ConfigLoader
import main.kotlin.analyzer.DefaultAnalyzer
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.lexer.Lexer
import main.kotlin.lexer.Token
import main.kotlin.parser.DefaultParser
import org.example.DefaultInterpreter
import org.example.formatter.Formatter
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import runner.RunnerResult.Analyze
import runner.RunnerResult.Format
import runner.RunnerResult.Validate
import java.io.File
import java.io.Reader
import kotlin.io.path.createTempFile
import kotlin.io.readText

class Runner(
    private val version: String,
    reader: Reader,
) {
    private val sourceCode: String = reader.readText()
    private val lexer: Lexer
    private val parser: DefaultParser
    private val analyzer = DefaultAnalyzer()
    private val formatter = Formatter
    private val tokens: List<Token>

    init {
        val lexerFactory = LexerFactoryRegistry.getFactory(version)
        val parserFactory = ParserFactoryRegistry.getFactory(version)

        lexer = lexerFactory.create()
        parser = parserFactory.create()
        tokens = lexer.tokenize(sourceCode)
    }

    fun execute(output: Output) {
        val provider =
            when (version) {
                "1.0" -> PreConfiguredProviders.VERSION_1_0
                "1.1" -> PreConfiguredProviders.VERSION_1_1
                else -> error("Unsupported version: $version")
            }

        val interpreter = DefaultInterpreter(output, provider)
        parser.parse(tokens).forEach(interpreter::interpret)
    }

    fun analyze(jsonFile: JsonObject): Analyze {
        val analyzerConfig =
            try {
                ConfigLoader.loadFromJsonString(jsonFile.toString())
            } catch (e: Exception) {
                return Analyze(
                    warnings = emptyList(),
                    errors = listOf(e.message ?: "Invalid analyzer configuration"),
                )
            }

        val ast =
            try {
                parser.parse(tokens)
            } catch (e: Exception) {
                return Analyze(
                    warnings = emptyList(),
                    errors = listOf(e.message ?: "Syntax error"),
                )
            }

        val diagnostics = analyzer.analyze(ast, analyzerConfig).diagnostics
        val warnings = diagnostics.filter { it.severity == DiagnosticSeverity.WARNING }.map { it.message }
        val errors = diagnostics.filter { it.severity == DiagnosticSeverity.ERROR }.map { it.message }

        return Analyze(warnings, errors)
    }

    fun format(json: String, version: String): Format {
        if (version != this.version) {
            return Format(
                formattedCode = "",
                errors = listOf("Formatter version mismatch: expected ${this.version} but got $version"),
            )
        }

        val ast =
            try {
                parser.parse(tokens)
            } catch (e: Exception) {
                return Format(
                    formattedCode = "",
                    errors = listOf(e.message ?: "Syntax error"),
                )
            }

        val existingConfigFile = File(json)
        val (configFile, tempFile) =
            if (existingConfigFile.exists() && existingConfigFile.isFile) {
                existingConfigFile to null
            } else {
                val temp = createTempFile(prefix = "formatter-config-", suffix = ".json").toFile()
                temp.writeText(json)
                temp.deleteOnExit()
                temp to temp
            }

        val formatted =
            try {
                formatter.formatMultiple(ast, configFile)
            } catch (e: Exception) {
                tempFile?.delete()
                return Format(
                    formattedCode = "",
                    errors = listOf(e.message ?: "Formatter error"),
                )
            }

        tempFile?.delete()
        return Format(formattedCode = formatted, errors = emptyList())
    }

    fun validate(): Validate =
        try {
            parser.parse(tokens)
            Validate(errors = emptyList())
        } catch (e: Exception) {
            Validate(errors = listOf(e.message ?: "Unknown error"))
        }
}
