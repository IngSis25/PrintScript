import lexer.ModifierType
import lexer.TokenRule
import main.kotlin.lexer.*

fun main() {
    val rules = listOf(
        TokenRule(Regex("\\Gval"), ModifierType),
        TokenRule(Regex("\\G[0-9]+"), LiteralNumber),
        TokenRule(Regex("\\G[A-Za-z_][A-Za-z0-9_]*"), IdentifierType),
        TokenRule(Regex("\\G="), OperatorType),
        TokenRule(Regex("\\G:"), PunctuationType),
        TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
        TokenRule(Regex("\\G;"), PunctuationType)

    )

    val tokenProvider = TokenProvider(rules)
    val lexer = DefaultLexer(tokenProvider)

    val code = "x = 2;"

    val tokens = lexer.tokenize(code)

    for (token in tokens) {
        println("${token.type} -> '${token.value}' (linea ${token.line}, col ${token.column})")
    }
}