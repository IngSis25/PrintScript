package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.AssignmentNode

class AssignmentKindCheck : SemanticCheck {
    override fun check(
        node: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    ) {
        // chequea que no sea const. Si es const, no se puede reasignar
        if (node.type == "AssignmentNode") {
            val assignmentNode = node as AssignmentNode
            val variableIdentifier = assignmentNode.identifier

            // busco el kind de la variable q estoy queriendo asignar, en la tabla.
            val declaredVariableKind = symbolTable[variableIdentifier.name]?.first
            if (declaredVariableKind == "const") {
                throw Exception("Cannot reassign a variable of type 'const'")
            }
        }
    }
}
