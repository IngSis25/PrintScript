package test.parserTest

import ConfiguredTokens
import factory.ParserFactoryV11
import main.kotlin.lexer.DefaultLexer
import org.example.ast.IdentifierBooleanNode
import org.example.ast.LiteralBooleanNode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BooleanParsingTest {
    @Test
    fun parserShouldParseTrueLiteral() {
        val input = "true"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is LiteralBooleanNode)

        val boolNode = ast[0] as LiteralBooleanNode
        assertEquals(true, boolNode.value)
    }

    @Test
    fun parserShouldParseFalseLiteral() {
        val input = "false"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is LiteralBooleanNode)

        val boolNode = ast[0] as LiteralBooleanNode
        assertEquals(false, boolNode.value)
    }

    @Test
    fun parserShouldParseBooleanIdentifier() {
        val input = "myBoolVar"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is IdentifierBooleanNode)

        val identNode = ast[0] as IdentifierBooleanNode
        assertEquals("myBoolVar", identNode.name)
    }
}
