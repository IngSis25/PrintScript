import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.google.gson.Gson
import com.google.gson.JsonObject
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.AnalyzerFactory
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import org.example.astnode.ASTNode
import java.io.File
import java.io.StringReader

class Analyze : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")
    private val rulePath by argument(help = "Path to the rule json to use")

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
        val rules = Gson().fromJson(rulesContent, JsonObject::class.java)

        echo("Lexing...\n", trailingNewline = true)
        val lexer = LexerFactory.createLexerV11(reader)

        echo("Parsing...\n", trailingNewline = true)
        val parser = ParserFactory.createParserV11(lexer)

        // Recolectar todos los nodos AST del parser
        val nodes = mutableListOf<ASTNode>()
        while (parser.hasNext()) {
            nodes.add(parser.next())
        }

        echo("Analyzing...\n", trailingNewline = true)
        // Crear un nuevo parser para el analyzer (que necesita un PrintScriptIterator)
        val lexer2 = LexerFactory.createLexerV11(StringReader(code))
        val parser2 = ParserFactory.createParserV11(lexer2)
        val analyzer = AnalyzerFactory().createAnalyzerV11(parser2)

        // Crear el config con el JsonObject
        val config = AnalyzerConfig(jsonConfig = rules)

        // Analizar los nodos
        val result = analyzer.analyze(nodes, config, "1.1")

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
