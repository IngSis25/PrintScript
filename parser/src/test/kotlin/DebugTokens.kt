import factory.LexerFactoryV11

fun main() {
    val code = "const name: string = readEnv(\"BEST_FOOTBALL_CLUB\");"

    val lexer = LexerFactoryV11().create()
    val tokens = lexer.tokenize(code)

    println("Tokens for: $code")
    tokens.forEach { token ->
        println("  ${token.type} = '${token.value}' (line ${token.line}, col ${token.column})")
    }
}
