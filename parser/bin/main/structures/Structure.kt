package structures

import org.example.Lexer.Token
import org.example.iterator.PrintScriptIterator

interface Structure {
    val type: String

    fun getTokens(
        tokenIterator: PrintScriptIterator<Token>,
        buffer: ArrayList<Token>,
    ): ArrayList<Token>

    fun checkStructure(str: Structure): Boolean
}
