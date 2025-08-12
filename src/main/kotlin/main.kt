import lexer.ModifierType
import lexer.TokenRule
import main.kotlin.lexer.*

fun main() {

    val baseProvider = ConfiguredTokens.providerV1()

    val ignored = listOf(
        TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),     // espacios/tabs
        TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true), // saltos de línea
        TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true) // comentarios //
    )


    val tokenProvider = TokenProvider(ignored + baseProvider.rules())
    val lexer = DefaultLexer(tokenProvider)

    val code = "x = 2;"

    val tokens = lexer.tokenize(code)

    for (token in tokens) {
        println("${token.type} -> '${token.value}' (linea ${token.line}, col ${token.column})")
    }
}