import factory.LexerFactoryRegistry
import main.kotlin.lexer.*

fun main(args: Array<String>) {
    // por default usa la version 1.0
    val version = args.getOrElse(0) { "1.0" }

    val factory = LexerFactoryRegistry.getFactory(version)
    val lexer = factory.create()

    val code = "x = 2;" // prueba con un codigo simple

    val tokens = lexer.tokenize(code)

    for (token in tokens) {
        println("${token.type} -> '${token.value}' (linea ${token.line}, col ${token.column})")
    }
}
