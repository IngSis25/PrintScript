package formatter

import config.FormatterConfig
import org.example.LiteralString
import org.example.ast.*

// FormatterVisitor SIMPLE
// Sin métodos visit separados, más directo y fácil de leer

data class FormatterVisitor(
    private val config: FormatterConfig,
    private val outputCode: StringBuilder,
) {
    fun evaluate(node: ASTNode) {
        when (node) {
            is VariableDeclarationNode -> {
                append("let ${node.identifier.name}")
                node.varType?.let { type ->
                    append(config.spaceAroundColonsRule().apply())
                    append(type)
                }
                node.value?.let { value ->
                    append(config.spaceAroundEqualsRule().apply())
                    evaluate(value)
                }
                endStatement()
            }

            is AssignmentNode -> {
                append(node.identifier.name)
                append(config.spaceAroundEqualsRule().apply())
                evaluate(node.value)
                endStatement()
            }

            is PrintlnNode -> {
                append(config.lineBreaksBeforePrintsRule().apply())
                append("println(")
                evaluate(node.value)
                append(")")
                endStatement()
            }

            is BinaryOpNode -> {
                handleExpression(node.left)
                append(" ${node.operator} ")
                handleExpression(node.right)
            }

            is LiteralNode -> {
                when (node.literalType) {
                    is LiteralString -> append("\"${node.value}\"")
                    else -> append(node.value)
                }
            }

            is IdentifierNode -> {
                append(node.name)
            }

            else -> throw IllegalArgumentException("Nodo no soportado: ${node::class}")
        }
    }

    // utility functions
    private fun append(string: String) {
        outputCode.append(string)
    }

    private fun endStatement() {
        outputCode.append(";\n")
    }

    private fun openExpression() {
        outputCode.append("(")
    }

    private fun closeExpression() {
        outputCode.append(")")
    }

    private fun handleExpression(node: ASTNode) {
        if (node is LiteralNode) {
            evaluate(node)
        } else if (node is IdentifierNode) {
            evaluate(node)
        } else if (node is BinaryOpNode) {
            openExpression()
            evaluate(node)
            closeExpression()
        } else {
            evaluate(node)
        }
    }
}
