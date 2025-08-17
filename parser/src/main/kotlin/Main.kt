import lexer.TokenRule
import main.kotlin.lexer.*            // Token, TokenProvider, DefaultLexer, ConfiguredTokens, etc.
import rules.RuleMatcher              // tu RuleMatcher que devuelve MatchedRule
import parser.rules.*                 // tus ParserRule: PrintlnRule, VariableDeclarationRule, ExpressionRule
import rules.PrintlnRule

fun main() {

    // 1) Configuración del LEXER (tus reglas + ignorados)
    val baseProvider = ConfiguredTokens.providerV1()

    val ignored = listOf(
        TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),      // espacios y tabs
        TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true), // saltos de línea
        TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true) // comentarios //
    )

    val tokenProvider = TokenProvider(ignored + baseProvider.rules())
    val lexer = DefaultLexer(tokenProvider)

    // ⚠️ IMPORTANTE: asegurate de tener "(" y ")" en ConfiguredTokens como PunctuationType:
    // "\\(" to PunctuationType,  "\\)" to PunctuationType

    // 2) Código de prueba (elegimos casos que tus reglas ya soportan)
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

    // 4) PARSER: armamos las reglas y el RuleMatcher
    val ruleMatcher = RuleMatcher(
        listOf(
            PrintlnRule(),
            VariableDeclarationRule(),
            ExpressionRule()
        )
    )

    val parser = DefaultParser(ruleMatcher)

    // 5) PARSE: construimos el AST
    val ast = parser.parse(tokens)

    println("\n=== AST ===")
    ast.forEach { println(it) }
}
