package test.analyzer

import main.kotlin.analyzer.SourcePosition
import main.kotlin.analyzer.SymbolTable
import main.kotlin.analyzer.TypeOf
import main.kotlin.analyzer.Types
import org.example.Lexer.Location
import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TypeOfTest {
    private val location = Location(1, 1)

    @Test
    fun testStringToTypeNumber() {
        assertEquals(Types.NUMBER, TypeOf.stringToType("number"))
        assertEquals(Types.NUMBER, TypeOf.stringToType("NUMBER"))
        assertEquals(Types.NUMBER, TypeOf.stringToType("Number"))
    }

    @Test
    fun testStringToTypeString() {
        assertEquals(Types.STRING, TypeOf.stringToType("string"))
        assertEquals(Types.STRING, TypeOf.stringToType("STRING"))
        assertEquals(Types.STRING, TypeOf.stringToType("String"))
    }

    @Test
    fun testStringToTypeBoolean() {
        assertEquals(Types.BOOLEAN, TypeOf.stringToType("boolean"))
        assertEquals(Types.BOOLEAN, TypeOf.stringToType("BOOLEAN"))
        assertEquals(Types.BOOLEAN, TypeOf.stringToType("Boolean"))
    }

    @Test
    fun testStringToTypeArray() {
        assertEquals(Types.ARRAY, TypeOf.stringToType("array"))
        assertEquals(Types.ARRAY, TypeOf.stringToType("ARRAY"))
        assertEquals(Types.ARRAY, TypeOf.stringToType("Array"))
    }

    @Test
    fun testStringToTypeUnknown() {
        assertEquals(Types.UNKNOWN, TypeOf.stringToType("unknown"))
        assertEquals(Types.UNKNOWN, TypeOf.stringToType("invalid"))
        assertEquals(Types.UNKNOWN, TypeOf.stringToType(""))
    }

    @Test
    fun testInferTypeFromNumberLiteral() {
        val literal = LiteralNode("Literal", location, LiteralValue.NumberValue(42))
        val symbolTable = SymbolTable()
        assertEquals(Types.NUMBER, TypeOf.inferType(literal, symbolTable))
    }

    @Test
    fun testInferTypeFromStringLiteral() {
        val literal = LiteralNode("Literal", location, LiteralValue.StringValue("test"))
        val symbolTable = SymbolTable()
        assertEquals(Types.STRING, TypeOf.inferType(literal, symbolTable))
    }

    @Test
    fun testInferTypeFromBooleanLiteral() {
        val literal = LiteralNode("Literal", location, LiteralValue.BooleanValue(true))
        val symbolTable = SymbolTable()
        assertEquals(Types.BOOLEAN, TypeOf.inferType(literal, symbolTable))
    }

    @Test
    fun testInferTypeFromNullLiteral() {
        val literal = LiteralNode("Literal", location, LiteralValue.NullValue)
        val symbolTable = SymbolTable()
        assertEquals(Types.NULL, TypeOf.inferType(literal, symbolTable))
    }

    @Test
    fun testInferTypeFromPromiseLiteral() {
        val literal = LiteralNode("Literal", location, LiteralValue.PromiseValue)
        val symbolTable = SymbolTable()
        assertEquals(Types.PROMISE, TypeOf.inferType(literal, symbolTable))
    }

    @Test
    fun testInferTypeFromIdentifier() {
        val symbolTable = SymbolTable()
        symbolTable.declare("x", Types.NUMBER, true, SourcePosition(1, 1))
        val identifier = IdentifierNode("Identifier", location, "x", "number", "let")
        assertEquals(Types.NUMBER, TypeOf.inferType(identifier, symbolTable))
    }

    @Test
    fun testInferTypeFromUndefinedIdentifier() {
        val symbolTable = SymbolTable()
        val identifier = IdentifierNode("Identifier", location, "undefinedVar", "", "let")
        assertEquals(Types.UNKNOWN, TypeOf.inferType(identifier, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionAdditionNumbers() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(1))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(2))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "+")
        assertEquals(Types.NUMBER, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionAdditionString() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.StringValue("hello"))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(2))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "+")
        assertEquals(Types.STRING, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionSubtraction() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "-")
        assertEquals(Types.NUMBER, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionMultiplication() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(2))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "*")
        assertEquals(Types.NUMBER, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionDivision() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(10))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(2))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "/")
        assertEquals(Types.NUMBER, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionComparison() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, ">")
        assertEquals(Types.BOOLEAN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionEquality() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "==")
        assertEquals(Types.BOOLEAN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionInequality() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "!=")
        assertEquals(Types.BOOLEAN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionLessThan() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "<")
        assertEquals(Types.BOOLEAN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionLessThanOrEqual() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "<=")
        assertEquals(Types.BOOLEAN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionGreaterThanOrEqual() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, ">=")
        assertEquals(Types.BOOLEAN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionInvalidOperator() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.NumberValue(5))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(3))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "invalid")
        assertEquals(Types.UNKNOWN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromBinaryExpressionStringSubtraction() {
        val symbolTable = SymbolTable()
        val left = LiteralNode("Literal", location, LiteralValue.StringValue("hello"))
        val right = LiteralNode("Literal", location, LiteralValue.NumberValue(2))
        val binary = BinaryExpressionNode("BinaryExpression", location, left, right, "-")
        assertEquals(Types.UNKNOWN, TypeOf.inferType(binary, symbolTable))
    }

    @Test
    fun testInferTypeFromUnknownNode() {
        val symbolTable = SymbolTable()
        // Crear un nodo mock que no es LiteralNode, IdentifierNode ni BinaryExpressionNode
        val unknownNode =
            object : org.example.astnode.ASTNode {
                override val type: String = "Unknown"
                override val location: Location = Location(1, 1)

                override fun accept(visitor: org.example.astnode.astNodeVisitor.ASTNodeVisitor) = org.example.astnode.astNodeVisitor.VisitorResult.Empty
            }
        assertEquals(Types.UNKNOWN, TypeOf.inferType(unknownNode, symbolTable))
    }
}
