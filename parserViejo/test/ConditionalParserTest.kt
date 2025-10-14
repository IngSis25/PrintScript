package test.parserTest

import ConfiguredTokens
import factory.ParserFactoryV11
import main.kotlin.lexer.DefaultLexer
import org.example.ast.BlockNode
import org.example.ast.IdentifierBooleanNode
import org.example.ast.IfNode
import org.example.ast.LiteralBooleanNode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConditionalParserTest {
    @Test
    fun parserShouldParseSimpleIfStatement() {
        val input = "if (condition) { println(\"hello\"); }"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is IfNode)

        val ifNode = ast[0] as IfNode
        assertTrue(ifNode.condition is IdentifierBooleanNode)
        assertTrue(ifNode.thenBlock is BlockNode)
        assertNull(ifNode.elseBlock)

        val conditionNode = ifNode.condition as IdentifierBooleanNode
        assertEquals("condition", conditionNode.name)
    }

    @Test
    fun parserShouldParseIfElseStatement() {
        val input = "if (isValid) { println(\"true\"); } else { println(\"false\"); }"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is IfNode)

        val ifNode = ast[0] as IfNode
        assertTrue(ifNode.condition is IdentifierBooleanNode)
        assertTrue(ifNode.thenBlock is BlockNode)
        assertNotNull(ifNode.elseBlock)
        assertTrue(ifNode.elseBlock is BlockNode)

        val conditionNode = ifNode.condition as IdentifierBooleanNode
        assertEquals("isValid", conditionNode.name)
    }

    @Test
    fun parserShouldParseIfWithBooleanLiteral() {
        val input = "if (true) { println(\"always\"); }"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is IfNode)

        val ifNode = ast[0] as IfNode
        assertTrue(ifNode.condition is LiteralBooleanNode)

        val conditionNode = ifNode.condition as LiteralBooleanNode
        assertEquals(true, conditionNode.value)
    }

    @Test
    fun parserShouldParseIfWithFalseLiteral() {
        val input = "if (false) { println(\"never\"); }"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        assertEquals(1, ast.size)
        assertTrue(ast[0] is IfNode)

        val ifNode = ast[0] as IfNode
        assertTrue(ifNode.condition is LiteralBooleanNode)

        val conditionNode = ifNode.condition as LiteralBooleanNode
        assertEquals(false, conditionNode.value)
    }

    @Test
    fun parserShouldFailOnIfWithoutCondition() {
        val input = "if () { println(\"error\"); }"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()

        assertThrows(RuntimeException::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun parserShouldFailOnIfWithoutBraces() {
        val input = "if (condition) println(\"error\");"
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()

        assertThrows(RuntimeException::class.java) {
            parser.parse(tokens)
        }
    }
}
