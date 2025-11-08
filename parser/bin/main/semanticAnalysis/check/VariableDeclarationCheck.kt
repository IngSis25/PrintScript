package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.VariableDeclarationNode
import semanticAnalysis.check.SemanticCheck

class VariableDeclarationCheck : SemanticCheck {
    override fun check(
        node: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    ) {
        // check that the variable being declared is not already declared
        if (node.type == "VariableDeclarationNode") {
            val variableDeclarationNode = node as VariableDeclarationNode
            val variableIdentifier = variableDeclarationNode.identifier
            if (symbolTable.containsKey(variableIdentifier.name)) {
                // caso donde ya existe la variable
                throw Exception("Variable ${variableIdentifier.name} ya fue declarada")
            }
        }
    }
}
