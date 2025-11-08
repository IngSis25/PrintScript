package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.CompleteIfNode

class CompleteIfBooleanExpressionCheck : SemanticCheck {
    override fun check(
        node: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    ) {
        if (node.type == "CompleteIfNode") {
            val completeIfNode = node as CompleteIfNode
            val condition = completeIfNode.ifNode.boolean

            val conditionType = condition.getType(symbolTable)

            if (conditionType != "boolean") {
                throw Exception(
                    "Illegal expression in if statement at ${completeIfNode.location}." +
                        " Condition is not of type boolean in ${condition.location}.",
                )
            }
        }
    }
}
