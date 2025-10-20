import main.kotlin.lexer.LexerFactoryV11
import factory.ParserFactoryV11

fun main() {
    val code = """
        const name: string = readEnv("BEST_FOOTBALL_CLUB");
    """.trimIndent()

    println("Testing code:")
    println(code)
    println()

    // Test lexer
    val lexer = LexerFactoryV11().create()
    val tokens = lexer.tokenize(code)
    println("Tokens:")
    tokens.forEach { token ->
        println("  ${token.type} = '${token.value}' (line ${token.line}, col ${token.column})")
    }
    println()

    // Test parser
    val parser = ParserFactoryV11().create()
    try {
        val ast = parser.parse(tokens)
        println("AST parsed successfully:")
        ast.forEach { node ->
            println("  $node")
        }
    } catch (e: Exception) {
        println("Parser error: ${e.message}")
        e.printStackTrace()
    }
}