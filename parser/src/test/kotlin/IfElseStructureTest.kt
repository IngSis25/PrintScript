package structures

import org.example.Lexer.Location
import org.example.Lexer.Token
import org.example.iterator.PrintScriptIterator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private class ListTokenIterator(private val tokens: List<Token>) : PrintScriptIterator<Token> {
    private var index = 0
    override fun hasNext(): Boolean = index < tokens.size
    override fun next(): Token? = if (hasNext()) tokens[index++] else null
    override fun peek(): Token? = if (hasNext()) tokens[index] else null
}

class IfElseStructureTest {

    private fun t(type: String, value: String = "", line: Int = 1, col: Int = 1) =
        Token(type, value, Location(line, col))

    @Test
    fun `separateIfElse splits tokens at separator`() {
        val structure = IfElseStructure()
        val tokens = listOf(
            t("IfToken"), t("OpenParenToken"), t("IdentifierToken"), t("CloseParenToken"),
            t("OpenBraceToken"), t("IdentifierToken"), t("SemicolonToken"), t("CloseBraceToken"),
            t("Separator"), // artificial separator
            t("ElseToken"),
            t("OpenBraceToken"), t("IdentifierToken"), t("SemicolonToken"), t("CloseBraceToken")
        )

        val (ifTokens, elseTokens) = structure.separateIfElse(tokens)
        assertTrue(ifTokens.isNotEmpty())
        assertTrue(elseTokens.isNotEmpty())
        // ensure separator not included
        assertFalse(ifTokens.any { it.type == "Separator" })
        assertFalse(elseTokens.any { it.type == "Separator" })
    }
}


