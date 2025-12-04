package semanticAnalysis.check

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.expressionNodes.ReadInputNode
import org.example.astnode.statamentNode.VariableDeclarationNode
import semanticAnalysis.check.SemanticCheck

class VariableDeclarationTypeCheck : SemanticCheck {
    override fun check(
        node: ASTNode,
        symbolTable: MutableMap<String, Pair<String, LiteralValue>>,
    ) {
        // chequeo que el tipo de la variable declarada sea el mismo que el tipo de la variable asignada
        if (node.type == "VariableDeclarationNode") {
            val variableDeclarationNode = node as VariableDeclarationNode
            val expressionNode = variableDeclarationNode.init
            val variableType = variableDeclarationNode.identifier.dataType
            val expressionType = expressionNode.getType(symbolTable)

            if (expressionType == "null") {
                return
            }

            if (expressionNode is ReadInputNode && expressionType != "string") {
                throw Exception("ReadInputNode message must be of type string")
            }

            // Permitir asignar readInput (que devuelve string) a variables de tipo number o boolean
            // porque el intérprete hace la conversión automática
            if (expressionNode is ReadInputNode && (variableType == "number" || variableType == "boolean")) {
                return // Permitir la asignación, el intérprete se encargará de la conversión
            }

            if (expressionType != variableType && expressionType != "Undefined") {
                throw Exception(
                    "Variable ${variableDeclarationNode.identifier.name}" +
                        " no es del tipo $variableType " +
                        "y no puede ser asignada con un valor de tipo $expressionType",
                )
            }
        }
    }
}
