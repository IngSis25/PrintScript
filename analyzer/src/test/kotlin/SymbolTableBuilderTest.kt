package test.analyzer

import main.kotlin.analyzer.SymbolTableBuilder
import main.kotlin.analyzer.Types
import org.example.Lexer.Location
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.VariableDeclarationNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SymbolTableBuilderTest {
    private val location = Location(1, 1)

    @Test
    fun testBuildEmptyTable() {
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        val table = SymbolTableBuilder.build(nodes)
        assertNotNull(table)
        assertTrue(table.getAllSymbols().isEmpty())
    }

    @Test
    fun testBuildTableWithNumberVariable() {
        val identifier = IdentifierNode("Identifier", location, "x", "number", "let")
        val init = LiteralNode("Literal", location, LiteralValue.NumberValue(42))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        assertEquals(Types.NUMBER, table.getType("x"))
    }

    @Test
    fun testBuildTableWithStringVariable() {
        val identifier = IdentifierNode("Identifier", location, "name", "string", "let")
        val init = LiteralNode("Literal", location, LiteralValue.StringValue("test"))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        assertEquals(Types.STRING, table.getType("name"))
    }

    @Test
    fun testBuildTableWithBooleanVariable() {
        val identifier = IdentifierNode("Identifier", location, "flag", "boolean", "let")
        val init = LiteralNode("Literal", location, LiteralValue.BooleanValue(true))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        assertEquals(Types.BOOLEAN, table.getType("flag"))
    }

    @Test
    fun testBuildTableWithInferredType() {
        val identifier = IdentifierNode("Identifier", location, "x", "", "let")
        val init = LiteralNode("Literal", location, LiteralValue.NumberValue(42))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        assertEquals(Types.NUMBER, table.getType("x"))
    }

    @Test
    fun testBuildTableWithMultipleVariables() {
        val identifier1 = IdentifierNode("Identifier", location, "x", "number", "let")
        val init1 = LiteralNode("Literal", location, LiteralValue.NumberValue(1))
        val varDecl1 = VariableDeclarationNode("VariableDeclaration", location, identifier1, init1, "let")
        val identifier2 = IdentifierNode("Identifier", Location(2, 1), "y", "string", "let")
        val init2 = LiteralNode("Literal", Location(2, 1), LiteralValue.StringValue("test"))
        val varDecl2 = VariableDeclarationNode("VariableDeclaration", Location(2, 1), identifier2, init2, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl1, varDecl2)
        val table = SymbolTableBuilder.build(nodes)
        assertEquals(Types.NUMBER, table.getType("x"))
        assertEquals(Types.STRING, table.getType("y"))
        assertEquals(2, table.getAllSymbols().size)
    }

    @Test
    fun testBuildTableWithExplicitType() {
        val identifier = IdentifierNode("Identifier", location, "x", "string", "let")
        val init = LiteralNode("Literal", location, LiteralValue.NumberValue(42))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        // Debería usar el tipo explícito, no el inferido
        assertEquals(Types.STRING, table.getType("x"))
    }

    @Test
    fun testBuildTableIgnoresNonVariableNodes() {
        val identifier = IdentifierNode("Identifier", location, "x", "number", "let")
        val init = LiteralNode("Literal", location, LiteralValue.NumberValue(42))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val otherNode = LiteralNode("Literal", location, LiteralValue.StringValue("test"))
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl, otherNode)
        val table = SymbolTableBuilder.build(nodes)
        assertEquals(1, table.getAllSymbols().size)
        assertTrue(table.contains("x"))
    }

    @Test
    fun testBuildTableWithPosition() {
        val line = 5
        val column = 10
        val varLocation = Location(line, column)
        val identifier = IdentifierNode("Identifier", varLocation, "x", "number", "let")
        val init = LiteralNode("Literal", varLocation, LiteralValue.NumberValue(42))
        val varDecl = VariableDeclarationNode("VariableDeclaration", varLocation, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        val info = table.getSymbolInfo("x")
        assertNotNull(info)
        assertEquals(line, info?.position?.line)
        assertEquals(column, info?.position?.column)
    }

    @Test
    fun testBuildTableWithNullLocationUsesDefault() {
        val identifier = IdentifierNode("Identifier", location, "x", "number", "let")
        val init = LiteralNode("Literal", location, LiteralValue.NumberValue(42))
        val varDecl = VariableDeclarationNode("VariableDeclaration", location, identifier, init, "let")
        val nodes = mutableListOf<org.example.astnode.ASTNode>(varDecl)
        val table = SymbolTableBuilder.build(nodes)
        assertNotNull(table)
        assertEquals(Types.NUMBER, table.getType("x"))
    }
}
