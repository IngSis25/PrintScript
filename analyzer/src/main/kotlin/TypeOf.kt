package main.kotlin.analyzer

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue

object TypeOf {
    fun stringToType(s: String): Types =
        when (s.lowercase()) {
            "number" -> Types.NUMBER
            "string" -> Types.STRING
            "boolean" -> Types.BOOLEAN
            "array" -> Types.ARRAY
            else -> Types.UNKNOWN
        }

    fun inferType(
        node: ASTNode,
        symbolTable: SymbolTable,
    ): Types =
        when (node) {
            is LiteralNode ->
                when (node.value) {
                    is LiteralValue.NumberValue -> Types.NUMBER
                    is LiteralValue.StringValue -> Types.STRING
                    is LiteralValue.BooleanValue -> Types.BOOLEAN
                    is LiteralValue.NullValue -> Types.NULL
                    is LiteralValue.PromiseValue -> Types.PROMISE
                }

            is IdentifierNode -> symbolTable.getType(node.name) ?: Types.UNKNOWN

            is BinaryExpressionNode -> {
                val leftType = inferType(node.left as ASTNode, symbolTable)
                val rightType = inferType(node.right as ASTNode, symbolTable)

                when (node.operator) {
                    "+" ->
                        when {
                            leftType == Types.STRING || rightType == Types.STRING -> Types.STRING
                            leftType == Types.NUMBER && rightType == Types.NUMBER -> Types.NUMBER
                            else -> Types.UNKNOWN
                        }
                    "-", "*", "/" ->
                        if (leftType == Types.NUMBER &&
                            rightType == Types.NUMBER
                        ) {
                            Types.NUMBER
                        } else {
                            Types.UNKNOWN
                        }
                    "==", "!=", ">", "<", ">=", "<=" -> Types.BOOLEAN
                    else -> Types.UNKNOWN
                }
            }

            else -> Types.UNKNOWN
        }
}
