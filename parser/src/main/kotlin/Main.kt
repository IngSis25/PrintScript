import lexer.TokenRule
import main.kotlin.lexer.*
import rules.RuleMatcher
import parser.rules.*
import rules.PrintlnRule

fun main() {

    val baseProvider = ConfiguredTokens.providerV1()

    val ignored = listOf(
        TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
        TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
        TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true)
    )

    val tokenProvider = TokenProvider(ignored + baseProvider.rules())
    val lexer = DefaultLexer(tokenProvider)


    val code = """
        let nombre = "Achu";
        println("hola");
        println(12 + 8);
    """.trimIndent()

    // 3) LEXER: tokenizamos
    val tokens = lexer.tokenize(code)

    println("=== TOKENS ===")
    for (t in tokens) {
        println("${t.type} -> '${t.value}' (linea ${t.line}, col ${t.column})")
    }

    val ruleMatcher = RuleMatcher(
        listOf(
            PrintlnRule(),
            VariableDeclarationRule(),
            ExpressionRule()
        )
    )

    val parser = DefaultParser(ruleMatcher)

    val ast = parser.parse(tokens)

    println("\n=== AST ===")
    ast.forEach { println(it) }
}
