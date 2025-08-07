
interface Lexer{
    fun tokenize(sourceCode: String): List<Token>
}