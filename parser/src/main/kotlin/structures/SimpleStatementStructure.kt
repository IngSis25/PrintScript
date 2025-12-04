package structures

import org.example.Lexer.Token
import org.example.iterator.PrintScriptIterator

class SimpleStatementStructure : Structure {
    override val type = "SemicolonToken"

    override fun getTokens(
        tokenIterator: PrintScriptIterator<Token>,
        buffer: ArrayList<Token>,
    ): ArrayList<Token> {
        // Cuando vemos un SemicolonToken, eso marca el final de la sentencia actual
        // El buffer ya contiene todos los tokens incluyendo el SemicolonToken
        // Simplemente retornamos el buffer tal como está
        return buffer
    }

    override fun checkStructure(str: Structure): Boolean {
        // No necesitamos validar el patrón aquí, el ASTGenerator lo hará
        return true
    }
}
