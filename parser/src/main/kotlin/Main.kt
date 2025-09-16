import main.kotlin.lexer.*
import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import rules.RuleMatcher
import types.PunctuationType

fun main() {
    val baseProvider = ConfiguredTokens.providerV1()

    val ignored =
        listOf(
            TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true), // espacios
            TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true), // saltos de línea
            TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true), // comentarios
        )

    val tokenProvider = TokenProvider(ignored + baseProvider.rules())
    val lexer = DefaultLexer(tokenProvider)

    val code =
        """
        // comentario inicial
        let name: string = "Joe";
        let lastName: string = "Doe";

        // expresión de impresión
        println(name + " " + lastName);
        """.trimIndent()

    val tokens = lexer.tokenize(code)

    println("=== TOKENS ===")
    for (t in tokens) {
        println("${t.type} -> '${t.value}' (linea ${t.line}, col ${t.column})")
    }

    val ruleMatcher = RuleMatcher(ConfiguredRules.V1)
    val parser = DefaultParser(ruleMatcher)

    val ast = parser.parse(tokens)

    println("\n=== AST ===")
    ast.forEach { println(it) }
}
