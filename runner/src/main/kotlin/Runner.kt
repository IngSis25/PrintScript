package runner

import factory.LexerFactoryRegistry
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.DefaultAnalyzer
import main.kotlin.analyzer.Diagnostic
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.lexer.Lexer
import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import org.example.ast.ASTNode
import org.example.formatter.Formatter
import rules.RuleMatcher
import java.io.File

class Runner(
    private val version: String,
    private val sourceCode: String,
) {
    private val lexer: Lexer = LexerFactoryRegistry.getFactory(version).create()
    private val tokens = lexer.tokenize(sourceCode)

    // Parser según versión de lenguaje
    private val parser: DefaultParser =
        when (version) {
            "1.0" -> {
                val rules = ConfiguredRules.V1
                DefaultParser(RuleMatcher(rules))
            }
            "1.1" -> {
                val dummy = DefaultParser(RuleMatcher(emptyList()))
                val rules = ConfiguredRules.createV11Rules(dummy)
                DefaultParser(RuleMatcher(rules))
            }
            else -> error("Versión de PrintScript no soportada: $version")
        }

    /** Intenta parsear el programa completo y devuelve errores de sintaxis si los hay. */
    fun validate(): RunnerResult.Validate =
        try {
            parser.parse(tokens)
            RunnerResult.Validate(errors = emptyList())
        } catch (e: Exception) {
            RunnerResult.Validate(errors = listOf(e.message ?: "Syntax error"))
        }

    /**
     * Analiza estáticamente (linter/analyzer).
     * Recibe tu AnalyzerConfig (con reglas como camel/snake y println restriction).
     */
    fun analyze(config: AnalyzerConfig): RunnerResult.Analyze {
        val warnings = mutableListOf<String>()
        val errors = mutableListOf<String>()

        val ast: List<ASTNode> =
            try {
                parser.parse(tokens)
            } catch (e: Exception) {
                // Si la sintaxis falla, lo reportamos como error y salimos.
                return RunnerResult.Analyze(
                    warnings = emptyList(),
                    errors = listOf("Syntax error: ${e.message ?: "unknown"}"),
                )
            }

        val analyzer = DefaultAnalyzer()
        val result = analyzer.analyze(ast, config)

        // Mapeo simple de diagnostics → warnings/errors en texto
        result.diagnostics.forEach { d: Diagnostic ->
            when (d.severity) {
                DiagnosticSeverity.WARNING -> warnings += d.message
                DiagnosticSeverity.ERROR -> errors += d.message
                else -> {}
            }
        }

        return RunnerResult.Analyze(warnings = warnings, errors = errors)
    }

    /**
     * Formatea usando tu visitor y configuración en archivo JSON (FormatterConfig).
     * Si preferís el modo "simple" de cortar por ';', usá Formatter.formatSource(sourceCode, jsonFile).
     */
    fun format(jsonConfigFile: File): RunnerResult.Format {
        val errors = mutableListOf<String>()

        val ast: List<ASTNode> =
            try {
                parser.parse(tokens)
            } catch (e: Exception) {
                return RunnerResult.Format(formattedCode = "", errors = listOf("Syntax error: ${e.message}"))
            }

        val formatted =
            try {
                Formatter.formatMultiple(ast, jsonConfigFile)
            } catch (e: Exception) {
                errors += "Formatter error: ${e.message}"
                ""
            }

        return RunnerResult.Format(formattedCode = formatted, errors = errors)
    }
}
