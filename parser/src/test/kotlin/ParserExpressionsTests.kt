import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.expressionNodes.ReadEnvNode
import org.example.astnode.expressionNodes.ReadInputNode
import org.example.astnode.statamentNode.IfNode
import org.example.astnode.statamentNode.PrintStatementNode
import org.example.astnode.statamentNode.VariableDeclarationNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.StringReader
import kotlin.test.Test

/**
 * Tests para todas las expresiones del parser
 * Cubre: BinaryExpressionNode, BooleanExpressionNode, ReadInputNode, ReadEnvNode
 */
class ParserExpressionsTests {
    @Test
    fun testBinaryExpressionAddition() {
        val code = "let x: number = 5 + 3;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is BinaryExpressionNode)
        val binaryExpr = varNode.init as BinaryExpressionNode
        assertEquals("+", binaryExpr.operator)
        assertTrue(binaryExpr.left is LiteralNode)
        assertTrue(binaryExpr.right is LiteralNode)
    }

    @Test
    fun testBinaryExpressionSubtraction() {
        val code = "let x: number = 10 - 3;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        val binaryExpr = varNode.init as BinaryExpressionNode
        assertEquals("-", binaryExpr.operator)
    }

    @Test
    fun testBinaryExpressionMultiplication() {
        val code = "let x: number = 5 * 3;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        val binaryExpr = varNode.init as BinaryExpressionNode
        assertEquals("*", binaryExpr.operator)
    }

    @Test
    fun testBinaryExpressionDivision() {
        val code = "let x: number = 10 / 2;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        val binaryExpr = varNode.init as BinaryExpressionNode
        assertEquals("/", binaryExpr.operator)
    }

    @Test
    fun testBinaryExpressionStringConcatenation() {
        val code = "let x: string = 'Hello' + 'World';"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is BinaryExpressionNode)
        val binaryExpr = varNode.init as BinaryExpressionNode
        assertTrue(binaryExpr.left is LiteralNode)
        assertTrue(binaryExpr.right is LiteralNode)
    }

    @Test
    fun testBinaryExpressionWithVariables() {
        val code = "let x: number = 5; let y: number = x + 3;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val secondVar = nodes[1] as VariableDeclarationNode
        assertTrue(secondVar.init is BinaryExpressionNode)
        val binaryExpr = secondVar.init as BinaryExpressionNode
        assertTrue(binaryExpr.left is IdentifierNode)
        assertEquals("x", (binaryExpr.left as IdentifierNode).name)
    }

    @Test
    fun testComplexBinaryExpression() {
        val code = "let x: number = 5 + 3 * 2;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is BinaryExpressionNode)
    }

    @Test
    fun testBooleanExpressionLiteral() {
        val code = "let x: boolean = true;"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is LiteralNode)
        val literal = varNode.init as LiteralNode
        assertTrue(literal.value is LiteralValue.BooleanValue)
        assertEquals(true, (literal.value as LiteralValue.BooleanValue).value)
    }

    @Test
    fun testBooleanExpressionWithIdentifier() {
        val code = "let flag: boolean = true; let x: boolean = flag;"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        val secondVar = nodes[1] as VariableDeclarationNode
        assertTrue(secondVar.init is IdentifierNode)
        assertEquals("flag", (secondVar.init as IdentifierNode).name)
    }

    @Test
    fun testReadInputNode() {
        val code = "let x: string = readInput('Enter name:');"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is ReadInputNode)
        val readInput = varNode.init as ReadInputNode
        assertTrue(readInput.message is LiteralNode)
        val message = readInput.message as LiteralNode
        assertEquals(LiteralValue.StringValue("Enter name:"), message.value)
    }

    @Test
    fun testReadInputNodeWithExpression() {
        val code = "let msg: string = 'Name:'; let x: string = readInput(msg);"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        val secondVar = nodes[1] as VariableDeclarationNode
        assertTrue(secondVar.init is ReadInputNode)
        val readInput = secondVar.init as ReadInputNode
        assertTrue(readInput.message is IdentifierNode)
    }

    @Test
    fun testReadEnvNode() {
        val code = "let path: string = readEnv('PATH');"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is ReadEnvNode)
        val readEnv = varNode.init as ReadEnvNode
        assertEquals("PATH", readEnv.variableName)
    }

    @Test
    fun testReadInputInPrintStatement() {
        val code = "println(readInput('Enter value:'));"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        val printNode = nodes[0] as PrintStatementNode
        assertTrue(printNode.value is ReadInputNode)
    }

    @Test
    fun testBooleanExpressionInIf() {
        val code = "if (true) { println('Hello'); }"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        assertEquals(1, nodes.size)
        val ifNode = nodes[0] as IfNode
        assertTrue(true)
    }

    @Test
    fun testMultipleBinaryOperations() {
        val code = "let x: number = 1 + 2 + 3;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        assertTrue(varNode.init is BinaryExpressionNode)
    }

    @Test
    fun testBooleanExpressionFalse() {
        val code = "let x: boolean = false;"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = parser.collectAllASTNodes()

        val varNode = nodes[0] as VariableDeclarationNode
        val literal = varNode.init as LiteralNode
        assertEquals(false, (literal.value as LiteralValue.BooleanValue).value)
    }
}
