import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.output.ConsoleOutput
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream

class Execute : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")

    override fun run() {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                echo("Error: File not found: $filePath", err = true)
                return
            }

            val totalBytes = file.length()
            val inputStream = ProgressInputStream(FileInputStream(file), totalBytes)
            val reader = BufferedReader(inputStream.reader())

            val lexer = LexerFactory.createLexerV11(reader)
            val parser = ParserFactory.createParserV11(lexer)
            val output = ConsoleOutput()
            val interpreter = InterpreterFactory.createInterpreterVersion11(output)

            interpreter.interpret(parser)

            echo("Execution successful")
        } catch (e: Exception) {
            echo("Error: ${e.message}", err = true)
            throw e
        }
    }
}
