import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import org.example.formatter.Formatter
import org.example.formatter.config.FormatterConfig
import java.io.File
import java.io.StringReader

class Format : CliktCommand() {
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
        val rules = JsonParser.parseString(rulesContent).asJsonObject

        echo("Lexing...\n", trailingNewline = true)
        val lexer = LexerFactory.createLexerV11(reader)

        echo("Parsing...\n", trailingNewline = true)
        val parser = ParserFactory.createParserV11(lexer)

        echo("Formatting...\n", trailingNewline = true)

        val config = createFormatterConfig(rules)
        val formatter = Formatter(config)

        val nodes = parser.collectAllASTNodes()

        val formattedCode = formatter.format(nodes.iterator())

        File(filePath).writeText(formattedCode)

        echo("Format successful")
    }

    private fun createFormatterConfig(json: JsonObject): FormatterConfig {
        val finalJson = JsonParser.parseString(json.toString()).asJsonObject

        json.get("space_before_colon")?.let {
            finalJson.add("enforce-spacing-before-colon-in-declaration", it)
        }

        json.get("space_after_colon")?.let {
            finalJson.add("enforce-spacing-after-colon-in-declaration", it)
        }

        json.get("space_around_equals")?.let {
            finalJson.add("enforce-spacing-around-equals", it)
        }

        json.get("newline_after_println")?.let {
            finalJson.add("line-breaks-after-println", it)
        }

        json.get("newline_before_println")?.let {
            finalJson.add("line-breaks-after-println", it)
        }

        json.get("if_brace_same_line")?.let {
            finalJson.add("if-brace-same-line", it)
        }

        json.get("same_line_for_if_brace")?.let {
            finalJson.add("if-brace-same-line", it)
        }

        json.get("indent_inside_if")?.let {
            finalJson.add("indent-inside-if", it)
        }

        json.get("number_of_spaces_indentation")?.let {
            finalJson.add("indent-inside-if", it)
        }

        val gson = Gson()
        return gson.fromJson(finalJson, FormatterConfig::class.java)
    }
}
