package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.google.gson.Gson
import com.google.gson.JsonObject
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.AnalyzerFactory
import main.kotlin.analyzer.ConfigLoader
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import org.example.astnode.ASTNode
import java.io.File
import java.io.StringReader

class Analyze : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")
    private val rulePath by argument(help = "Path to the rule json to use")
    private val version by option(
        "--version",
        "-v",
        help = "PrintScript version to use (1.0 or 1.1)",
    ).choice("1.0", "1.1", ignoreCase = true).default("1.1")

    override fun run() {
        val file = File(filePath)
        if (!file.exists()) {
            echo("Error: File not found: $filePath", err = true)
            return
        }

        val rulesFile = File(rulePath)
        if (!rulesFile.exists()) {
            echo("Error: Rules file not found: $rulePath", err = true)
            return
        }

        val code = file.readText()
        val reader = StringReader(code)
        val rulesContent = rulesFile.readText()
        val rulesJson = Gson().fromJson(rulesContent, JsonObject::class.java)

        // Cargar la configuración completa usando ConfigLoader
        val config =
            try {
                main.kotlin.analyzer.ConfigLoader
                    .loadFromJson(rulesFile)
            } catch (e: Exception) {
                // Si falla el ConfigLoader, usar el JSON crudo (para compatibilidad con formato antiguo)
                AnalyzerConfig(jsonConfig = rulesJson)
            }

        echo("Lexing...\n", trailingNewline = true)
        val lexer =
            when (version) {
                "1.0" -> LexerFactory.createLexerV10(reader)
                "1.1" -> LexerFactory.createLexerV11(reader)
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }

        echo("Parsing...\n", trailingNewline = true)
        val parser =
            when (version) {
                "1.0" -> ParserFactory.createParserV10(lexer)
                "1.1" -> ParserFactory.createParserV11(lexer)
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }

        // Recolectar todos los nodos AST del parser
        val nodes = mutableListOf<ASTNode>()
        while (parser.hasNext()) {
            nodes.add(parser.next())
        }

        echo("Analyzing...\n", trailingNewline = true)
        // Crear un nuevo parser para el analyzer (que necesita un PrintScriptIterator)
        val lexer2 =
            when (version) {
                "1.0" -> LexerFactory.createLexerV10(StringReader(code))
                "1.1" -> LexerFactory.createLexerV11(StringReader(code))
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }
        val parser2 =
            when (version) {
                "1.0" -> ParserFactory.createParserV10(lexer2)
                "1.1" -> ParserFactory.createParserV11(lexer2)
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }
        val analyzer =
            when (version) {
                "1.0" -> AnalyzerFactory().createAnalyzerV10(parser2)
                "1.1" -> AnalyzerFactory().createAnalyzerV11(parser2)
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }

        // Analizar los nodos
        val result = analyzer.analyze(nodes, config, version)

        // Mostrar los diagnósticos
        result.diagnostics.forEach { diagnostic ->
            echo(
                "${diagnostic.severity}: ${diagnostic.message} (${diagnostic.position})",
                err =
                    diagnostic.severity == DiagnosticSeverity.ERROR,
            )
        }

        if (result.success) {
            echo("Analyze successful")
        } else {
            echo("Analyze completed with errors", err = true)
        }
    }
}
