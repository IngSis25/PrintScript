import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import org.ParserFactory
import java.io.File
import java.io.StringReader

class Validation : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")

    override fun run() {
        val code = File(filePath).readText()
        val reader = StringReader(code)

        echo("Analyzing...\n", trailingNewline = true)
        val lexer = LexerFactory.createLexerV11(reader)

        echo("Parsing...\n", trailingNewline = true)
        val parser = ParserFactory.createParserV11(lexer)
        parser.collectAllASTNodes()

        echo("Validation successful")
    }
}
