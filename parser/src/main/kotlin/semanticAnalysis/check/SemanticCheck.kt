package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.LiteralValue

interface SemanticCheck {
    fun check(
        node: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    )
}
