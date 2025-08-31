import builders.PrintBuilder
import lexer.TokenRule
import main.kotlin.lexer.*
import parser.rules.*
import rules.PrintlnRule
import rules.RuleMatcher

fun main() {
    val baseProvider = ConfiguredTokens.providerV1()

    val ignored =
        listOf(
            TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
            TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
            TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
        )

    val tokenProvider = TokenProvider(ignored + baseProvider.rules())
    val lexer = DefaultLexer(tokenProvider)

    val code =
        """
        let nombre: string = "Achu";
        println("hola");
        println(12 + 8);
        """.trimIndent()

    // LEXER
    val tokens = lexer.tokenize(code)

    println("=== TOKENS ===")
    for (t in tokens) {
        println("${t.type} -> '${t.value}' (linea ${t.line}, col ${t.column})")
    }

    // Reglas del parser (¡inyectando builders!)
    val ruleMatcher =
        RuleMatcher(
            listOf(
                // Importante: reglas específicas antes que las genéricas
                PrintlnRule(PrintBuilder()),
                // VariableDeclarationRule(VariableDeclarationBuilder()),
                // ExpressionRule(ExpressionBuilder()),
            ),
        )

    val parser = DefaultParser(ruleMatcher)

    val ast = parser.parse(tokens)

    println("\n=== AST ===")
    ast.forEach { println(it) }
}
