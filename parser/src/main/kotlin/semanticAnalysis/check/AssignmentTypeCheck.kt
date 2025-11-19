package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.expressionNodes.ReadInputNode
import org.example.astnode.statamentNode.AssignmentNode
import semanticAnalysis.check.SemanticCheck

class AssignmentTypeCheck : SemanticCheck {
    override fun check(
        node: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    ) {
        if (node.type == "AssignmentNode") {
            val assignmentNode = node as AssignmentNode
            val variableIdentifier = assignmentNode.identifier
            val expression = assignmentNode.value

            val variableType = symbolTable[variableIdentifier.name]?.second?.getType()
            val expressionType = expression.getType(symbolTable)

            // Permitir asignar readInput (que devuelve string) a variables de tipo number o boolean
            // porque el intérprete hace la conversión automática
            if (expression is ReadInputNode &&
                variableType != null &&
                (variableType == "number" || variableType == "boolean")
            ) {
                return // Permitir la asignación, el intérprete se encargará de la conversión
            }

            if (variableType != expressionType && variableType != "null") {
                throw Exception("Variable ${variableIdentifier.name} no es del tipo $variableType")
            }
        }
    }
}
