package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.expressionNodes.ReadInputNode

class ReadInputTypeCheck : SemanticCheck {
    override fun check(
        node: ASTNode,
        symbolTable: MutableMap<
            String,
            Pair<String, LiteralValue>,
        >,
    ) {
        if (node is ReadInputNode) {
            val messageType = node.message.getType(symbolTable)
            if (messageType != "string") {
                throw Exception("ReadInputNode message must be of type string")
            }
        }
    }

    private fun getExpressionType(
        expression: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    ): String =
        when (expression) {
            is LiteralValue -> expression.type
            is IdentifierNode ->
                symbolTable[expression.name]?.first ?: throw Exception(
                    "Variable ${expression.name} not declared",
                )
            is BinaryExpressionNode -> {
                val leftType = getExpressionType(expression.left, symbolTable)
                val rightType = getExpressionType(expression.right, symbolTable)

                // Check if both types are strings, or a combination of string and number
                if (
                    (leftType == "string" && rightType == "string") ||
                    (leftType == "string" && rightType == "number") ||
                    (leftType == "number" && rightType == "string")
                ) {
                    "string"
                } else {
                    throw Exception("Invalid expression type")
                }
            }
            else -> throw Exception("Invalid expression type")
        }
}
