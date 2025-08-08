import main.Lexer.Lexer

class DefaultLexer(private val tokenProvider : TokenProvider) : Lexer {
    override fun tokenize(sourceCode: String): List<Token> {
        // Implementation of the tokenize method
        return listOf()  // Placeholder return


    }
}
