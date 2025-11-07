import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.AssignmentNode
import org.example.astnode.statamentNode.PrintStatementNode
import org.example.astnode.statamentNode.VariableDeclarationNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * Tests para statements básicos del parser V10 y V11
 * Cubre: PrintStatementNode, VariableDeclarationNode, AssignmentNode
 */
class ParserBasicStatementsTest {
    @Test
    fun testPrintStatementWithNumber() {
        val code = "println(42);"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is PrintStatementNode)
        val printNode = nodes[0] as PrintStatementNode
        assertTrue(printNode.value is LiteralNode)
        val literal = printNode.value as LiteralNode
        assertEquals(LiteralValue.NumberValue(42), literal.value)
    }

    @Test
    fun testPrintStatementWithString() {
        val code = "println('Hello');"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is PrintStatementNode)
        val printNode = nodes[0] as PrintStatementNode
        assertTrue(printNode.value is LiteralNode)
        val literal = printNode.value as LiteralNode
        assertEquals(LiteralValue.StringValue("Hello"), literal.value)
    }

    @Test
    fun testPrintStatementWithExpression() {
        val code = "println(5 + 3);"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is PrintStatementNode)
        val printNode = nodes[0] as PrintStatementNode
        assertTrue(printNode.value is BinaryExpressionNode)
    }

    @Test
    fun testVariableDeclarationWithInitialization() {
        val code = "let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is VariableDeclarationNode)
        val varNode = nodes[0] as VariableDeclarationNode
        assertEquals("x", varNode.identifier.name)
        assertEquals("number", varNode.identifier.dataType)
        assertEquals("let", varNode.kind)
        assertTrue(varNode.init is LiteralNode)
        val init = varNode.init as LiteralNode
        assertEquals(LiteralValue.NumberValue(10), init.value)
    }

    @Test
    fun testVariableDeclarationWithoutInitialization() {
        val code = "let y: string;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is VariableDeclarationNode)
        val varNode = nodes[0] as VariableDeclarationNode
        assertEquals("y", varNode.identifier.name)
        assertEquals("string", varNode.identifier.dataType)
        assertTrue(varNode.init is LiteralNode)
        val init = varNode.init as LiteralNode
        assertEquals(LiteralValue.NullValue, init.value)
    }

    @Test
    fun testVariableDeclarationWithExpression() {
        val code = "let z: number = 5 + 3;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is VariableDeclarationNode)
        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is BinaryExpressionNode)
    }

    @Test
    fun testAssignmentNode() {
        val code = "let x: number = 0; x = 42;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(2, nodes.size)
        assertTrue(nodes[1] is AssignmentNode)
        val assignNode = nodes[1] as AssignmentNode
        assertEquals("x", assignNode.identifier.name)
        assertTrue(assignNode.value is LiteralNode)
        val value = assignNode.value as LiteralNode
        assertEquals(LiteralValue.NumberValue(42), value.value)
    }

    @Test
    fun testAssignmentWithExpression() {
        val code = "let x: number = 0; x = 10 + 20;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(2, nodes.size)
        assertTrue(nodes[1] is AssignmentNode)
        val assignNode = nodes[1] as AssignmentNode
        assertTrue(assignNode.value is BinaryExpressionNode)
    }

    @Test
    fun testMultipleStatements() {
        val code = "let x: number = 10; let y: string = 'hello'; println(x);"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(3, nodes.size)
        assertTrue(nodes[0] is VariableDeclarationNode)
        assertTrue(nodes[1] is VariableDeclarationNode)
        assertTrue(nodes[2] is PrintStatementNode)
    }

    @Test
    fun testPrintStatementWithoutSemicolon() {
        val code = "println(42)"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)

        val exception =
            assertFailsWith<Exception> {
                parser.collectAllASTNodes()
            }
        assertTrue(exception.message?.contains("Unexpected end of input") == true)
    }

    @Test
    fun testInvalidStatement() {
        val code = "let let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)

        val exception =
            assertFailsWith<Exception> {
                parser.collectAllASTNodes()
            }
        assertTrue(exception.message?.contains("No formula matches") == true)
    }

    @Test
    fun testPrintStatementWithVariable() {
        val code = "let x: number = 5; println(x);"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(2, nodes.size)
        assertTrue(nodes[1] is PrintStatementNode)
        val printNode = nodes[1] as PrintStatementNode
        assertTrue(printNode.value is IdentifierNode)
        val identifier = printNode.value as IdentifierNode
        assertEquals("x", identifier.name)
    }

    @Test
    fun testConstDeclaration() {
        val code = "const PI: number = 3.14;"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        assertTrue(nodes[0] is VariableDeclarationNode)
        val varNode = nodes[0] as VariableDeclarationNode
        assertEquals("const", varNode.kind)
    }

    @Test
    fun testParserHasNext() {
        val code = "let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)

        assertTrue(parser.hasNext())
        val node = parser.next()
        assertNotNull(node)
        assertTrue(parser.hasNext() == false || !lexer.hasNext())
    }

    @Test
    fun testParserPeek() {
        val code = "let x: number = 10; let y: number = 20;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)

        val peekedNode = parser.peek()
        assertNotNull(peekedNode)
        val nextNode = parser.next()
        assertEquals(peekedNode.type, nextNode.type)
    }
}
