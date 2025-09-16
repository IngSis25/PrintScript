package org.example.formatter

import org.example.LiteralString
import org.example.ast.*
import org.example.formatter.config.FormatterConfig
import org.example.formatter.rule.SpaceAroundColons

// org.example.formatter.FormatterVisitor SIMPLE
// Sin métodos visit separados, más directo y fácil de leer

data class FormatterVisitor(
    private val config: FormatterConfig,
    private val outputCode: StringBuilder,
) {
    private var indentLevel = 0
    private var atLineStart = true

    private fun indentString(): String = " ".repeat(config.indentSize * indentLevel)

    private fun writeIndentIfLineStart() {
        if (atLineStart) {
            outputCode.append(indentString())
            atLineStart = false
        }
    }

    private fun newLine() {
        outputCode.append("\n")
        atLineStart = true
    }

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
                writeIndentIfLineStart()
                append("let ${node.identifier.name}")
                node.varType?.let { type ->
                    val colonRule =
                        SpaceAroundColons(
                            spaceBefore = config.spaceBeforeColon,
                            spaceAfter = config.spaceAfterColon,
                        )
                    append(colonRule.apply())
                    append(type)
                }
                node.value?.let { value ->
                    append(config.spaceAroundAssignmentRule().apply())
                    evaluate(value)
                }
                endStatement()
            }

            is AssignmentNode -> {
                writeIndentIfLineStart()
                append(node.identifier.name)
                append(config.spaceAroundAssignmentRule().apply())
                evaluate(node.value)
                endStatement()
            }

            is PrintlnNode -> {
                writeIndentIfLineStart()
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
            is IfNode -> {
                // if (...) {  ← llave en misma línea si así lo pide la config
                writeIndentIfLineStart()
                val p = config.spaceInsideParenthesesRule()
                append("if ")
                append(p.applyOpen())
                evaluate(node.condition)
                append(p.applyClose())

                if (config.ifBraceSameLine) {
                    append(" {")
                    newLine()
                } else {
                    newLine()
                    writeIndentIfLineStart()
                    append("{")
                    newLine()
                }

                // Cuerpo del then con indentación
                indentLevel++
                when (val thenPart = node.thenBlock) {
                    is BlockNode -> evaluate(thenPart) // delega al case de BlockNode
                    else -> {
                        writeIndentIfLineStart()
                        evaluate(thenPart) // statements simples (println, let, assign, etc.)
                    }
                }
                indentLevel--

                // Cerrar bloque del then
                writeIndentIfLineStart()
                append("}")
                newLine()

                // else (opcional)
                node.elseBlock?.let { elsePart ->
                    writeIndentIfLineStart()
                    append("else")
                    when (elsePart) {
                        is IfNode -> {
                            append(" ")
                            // soporta "else if (...) { ... }"
                            evaluate(elsePart)
                        }
                        is BlockNode -> {
                            append(" {")
                            newLine()
                            indentLevel++
                            evaluate(elsePart)
                            indentLevel--
                            writeIndentIfLineStart()
                            append("}")
                            newLine()
                        }
                        else -> {
                            append(" {")
                            newLine()
                            indentLevel++
                            writeIndentIfLineStart()
                            evaluate(elsePart)
                            indentLevel--
                            writeIndentIfLineStart()
                            append("}")
                            newLine()
                        }
                    }
                }
            }

            is BlockNode -> {
                node.statements.forEach { stmt ->
                    writeIndentIfLineStart()
                    evaluate(stmt)
                }
            }

            else -> throw IllegalArgumentException("Nodo no soportado: ${node::class}")
        }
    }

    // utility functions
    private fun append(string: String) {
        outputCode.append(string)
    }

    private fun endStatement() {
        outputCode.append(";")
        newLine()
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
