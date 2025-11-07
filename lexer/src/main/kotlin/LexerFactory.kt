import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import java.io.Reader

object LexerFactory {
    fun createLexerV10(reader: Reader): Lexer = Lexer(TokenFactory().createLexerV10(), reader)

    fun createLexerV11(reader: Reader): Lexer = Lexer(TokenFactory().createLexerV11(), reader)
}
