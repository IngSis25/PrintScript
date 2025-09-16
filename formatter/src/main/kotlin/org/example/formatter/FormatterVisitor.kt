package org.example.formatter

import org.example.LiteralString
import org.example.ast.*
import org.example.formatter.config.FormatterConfig

// org.example.formatter.FormatterVisitor SIMPLE
// Sin métodos visit separados, más directo y fácil de leer

data class FormatterVisitor(
    private val config: FormatterConfig,
    private val outputCode: StringBuilder,
) {
    fun evaluateMultiple(nodes: List<ASTNode>) {
        for (i in nodes.indices) {
            val node = nodes[i]
            evaluate(node)

            // Si es un PrintlnNode y hay más nodos después, aplicar line breaks
            if (node is PrintlnNode && i < nodes.size - 1) {
                append(config.lineBreaksBeforePrintsRule().apply())
            }
        }
    }

    fun evaluate(node: ASTNode) {
        when (node) {
            is VariableDeclarationNode -> {
                append("let ${node.identifier.name}")
                node.varType?.let { type ->
                    append(config.spaceAroundColonsRule().apply())
                    append(type)
                }
                node.value?.let { value ->
                    append(config.spaceAroundAssignmentRule().apply())
                    evaluate(value)
                }
                endStatement()
            }

            is AssignmentNode -> {
                append(node.identifier.name)
                append(config.spaceAroundAssignmentRule().apply())
                evaluate(node.value)
                endStatement()
            }

            is PrintlnNode -> {
                val parenthesesRule = config.spaceInsideParenthesesRule()
                append("println")
                append(parenthesesRule.applyOpen())
                evaluate(node.value)
                append(parenthesesRule.applyClose())
                endStatement()
            }

            is BinaryOpNode -> {
                val op = node.operator
                printMaybeParens(op, node.left, isRightChild = false)
                append(" $op ")
                printMaybeParens(op, node.right, isRightChild = true)
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
        evaluate(node)
    }

    private fun printMaybeParens(
        parentOp: String,
        child: ASTNode,
        isRightChild: Boolean,
    ) {
        handleExpression(child)
    }
}
