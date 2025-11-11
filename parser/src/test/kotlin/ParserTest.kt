package org

import ASTGenerator
import ASTNodeBuilder
import org.example.Lexer.Location
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.iterator.PrintScriptIterator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import semanticAnalysis.SemanticAnalyzer

private class ListTokenIterator(private val tokens: List<Token>) : PrintScriptIterator<Token> {
    private var index = 0
    override fun hasNext(): Boolean = index < tokens.size
    override fun next(): Token? = if (hasNext()) tokens[index++] else null
    override fun peek(): Token? = if (hasNext()) tokens[index] else null
}

private class DummyAstNode(
    override val type: String = "Dummy",
    override val location: Location = Location(1, 1),
) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = VisitorResult.Empty
}

private class AcceptAllBuilder : ASTNodeBuilder {
    override val formula: String = "ANY"

    override fun generate(tokens: List<Token>, parser: Parser): ASTNode =
        DummyAstNode(location = tokens.firstOrNull()?.location ?: Location(1, 1))

    override fun checkFormula(tokensString: String): Boolean = tokensString.endsWith("SemicolonToken")
}

class ParserTest {

    private fun createParser(tokens: List<Token>): Parser {
        val iterator = ListTokenIterator(tokens)
        val astGenerator = ASTGenerator(listOf(AcceptAllBuilder()))
        val semanticAnalyzer = SemanticAnalyzer(emptyList())
        return Parser(
            astGenerator = astGenerator,
            semanticAnalyzer = semanticAnalyzer,
            supportedStructures = emptyList(),
            tokenIterator = iterator,
        )
    }

    private fun t(type: String, value: String = "", line: Int = 1, col: Int = 1) =
        Token(type, value, Location(line, col))

    @Test
    fun `parse single simple statement returns one node`() {
        val tokens =
            listOf(
                t("IdentifierToken", "x"),
                t("AssignmentToken", "="),
                t("NumberToken", "1"),
                t("SemicolonToken", ";"),
            )
        val parser = createParser(tokens)
        val node = parser.next()
        assertNotNull(node)
        assertEquals("Dummy", node.type)
    }

    @Test
    fun `peek does not consume and next consumes`() {
        val tokens =
            listOf(
                t("IdentifierToken", "x"),
                t("AssignmentToken", "="),
                t("NumberToken", "1"),
                t("SemicolonToken", ";"),
            )
        val parser = createParser(tokens)
        val firstPeek = parser.peek()
        val secondPeek = parser.peek()
        val firstNext = parser.next()
        assertNotNull(firstPeek)
        assertNotNull(secondPeek)
        assertEquals(firstPeek, secondPeek)
        assertEquals(firstPeek, firstNext)
    }

    @Test
    fun `collectAllASTNodes collects multiple statements`() {
        val tokens =
            listOf(
                t("IdentifierToken", "x"),
                t("AssignmentToken", "="),
                t("NumberToken", "1"),
                t("SemicolonToken", ";"),
                t("IdentifierToken", "y"),
                t("AssignmentToken", "="),
                t("NumberToken", "2"),
                t("SemicolonToken", ";"),
            )
        val parser = createParser(tokens)
        val nodes = parser.collectAllASTNodes()
        assertEquals(2, nodes.size)
    }

    @Test
    fun `missing semicolon throws syntactic error`() {
        val tokens =
            listOf(
                t("IdentifierToken", "x"),
                t("AssignmentToken", "="),
                t("NumberToken", "1"),
                // missing ;
            )
        val parser = createParser(tokens)
        val ex =
            assertThrows(Exception::class.java) {
                parser.next()
            }
        assertNotNull(ex.message)
    }
}


