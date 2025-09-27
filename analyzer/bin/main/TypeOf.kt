package main.kotlin.analyzer

import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*

/**
 * Type inference and checking utilities
 */
object TypeOf {
    /**
     * Infers the type of a literal node
     */
    fun literal(node: LiteralNode): Types =
        when (node.literalType) {
            is LiteralNumber -> Types.NUMBER
            is LiteralString -> Types.STRING
            else -> Types.UNKNOWN
        }

    /**
     * Infers the type of an identifier from the symbol table
     */
    fun identifier(
        node: IdentifierNode,
        symbolTable: SymbolTable,
    ): Types = symbolTable.get(node.name) ?: Types.UNKNOWN

    /**
     * Infers the type of a binary operation
     */
    fun binaryOperation(
        node: BinaryOpNode,
        symbolTable: SymbolTable,
    ): Types {
        val leftType = inferType(node.left, symbolTable)
        val rightType = inferType(node.right, symbolTable)

        return when (node.operator) {
            "+" -> {
                when {
                    leftType == Types.NUMBER && rightType == Types.NUMBER -> Types.NUMBER
                    leftType == Types.STRING && rightType == Types.STRING -> Types.STRING
                    else -> Types.UNKNOWN
                }
            }
            "-", "*", "/" -> {
                when {
                    leftType == Types.NUMBER && rightType == Types.NUMBER -> Types.NUMBER
                    else -> Types.UNKNOWN
                }
            }
            "==", "!=", "<", ">", "<=", ">=" -> {
                when {
                    leftType == rightType && leftType != Types.UNKNOWN -> Types.BOOLEAN
                    else -> Types.UNKNOWN
                }
            }
            "&&", "||" -> {
                when {
                    leftType == Types.BOOLEAN && rightType == Types.BOOLEAN -> Types.BOOLEAN
                    else -> Types.UNKNOWN
                }
            }
            else -> Types.UNKNOWN
        }
    }

    /**
     * Infers the type of any AST node
     */
    fun inferType(
        node: ASTNode,
        symbolTable: SymbolTable,
    ): Types =
        when (node) {
            is LiteralNode -> literal(node)
            is IdentifierNode -> identifier(node, symbolTable)
            is BinaryOpNode -> binaryOperation(node, symbolTable)
            else -> Types.UNKNOWN
        }

    /**
     * Checks if an operation can be performed between two types
     */
    fun canPerformOperation(
        operator: String,
        leftType: Types,
        rightType: Types,
    ): Boolean =
        when (operator) {
            "+" -> {
                (leftType == Types.NUMBER && rightType == Types.NUMBER) ||
                    (leftType == Types.STRING && rightType == Types.STRING)
            }
            "-", "*", "/" -> {
                leftType == Types.NUMBER && rightType == Types.NUMBER
            }
            "==", "!=", "<", ">", "<=", ">=" -> {
                leftType == rightType && leftType != Types.UNKNOWN
            }
            "&&", "||" -> {
                leftType == Types.BOOLEAN && rightType == Types.BOOLEAN
            }
            else -> false
        }

    /**
     * Checks if two types are compatible for assignment
     */
    fun isCompatible(
        declaredType: Types,
        assignedType: Types,
    ): Boolean =
        declaredType == assignedType ||
            declaredType == Types.UNKNOWN ||
            assignedType == Types.UNKNOWN

    /**
     * Checks if a node represents a simple expression (identifier or literal)
     */
    fun isSimpleExpression(node: ASTNode): Boolean = node is IdentifierNode || node is LiteralNode

    /**
     * Checks if a node represents a complex expression (binary operations, etc.)
     */
    fun isComplexExpression(node: ASTNode): Boolean = !isSimpleExpression(node)
}
