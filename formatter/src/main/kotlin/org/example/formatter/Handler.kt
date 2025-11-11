package org.example.formatter

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.BooleanExpressionNode
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.ReadEnvNode
import org.example.astnode.expressionNodes.ReadInputNode
import org.example.astnode.statamentNode.AssignmentNode
import org.example.astnode.statamentNode.CompleteIfNode
import org.example.astnode.statamentNode.IfNode
import org.example.astnode.statamentNode.PrintStatementNode
import org.example.astnode.statamentNode.VariableDeclarationNode
import org.example.formatter.config.FormatterConfig

class Handler(
    private val config: FormatterConfig,
    private val outputCode: StringBuilder,
    private val visitor: FormatterVisitor,
) {
    private var indentLevel = 0

    fun evaluate(node: ASTNode) {
        visitor.visit(node)
    }

    fun handleBinaryExpression(node: BinaryExpressionNode) {
        handleExpression(node.left, visitor, outputCode)
        append(" ${node.operator} ", outputCode)
        handleExpression(node.right, visitor, outputCode)
    }

    private fun handleExpression(
        node: ASTNode,
        visitor: FormatterVisitor,
        outputCode: StringBuilder,
    ) {
        when (node) {
            is LiteralNode -> visitor.visit(node)
            is IdentifierNode -> append(node.name, outputCode)
            is BinaryExpressionNode -> {
                // openExpression(outputCode)
                visitor.visit(node)
                // closeExpression(outputCode)
            }
            is BooleanExpressionNode -> evaluate(node.bool)
            is ReadEnvNode -> {
                append("readEnv(", outputCode)
                append(node.variableName, outputCode)
                append(")", outputCode)
            }
            is ReadInputNode -> {
                append("readInput(", outputCode)
                evaluate(node.message)
                append(")", outputCode)
            }
            else -> {
                if (node is ExpressionNode) {
                    evaluate(node)
                }
            }
        }
    }

    fun handleLiteral(node: LiteralNode) {
        when (val value = node.value) {
            is org.example.astnode.expressionNodes.LiteralValue.StringValue -> {
                // Agregar comillas simples alrededor del string
                append("\"", outputCode)
                append(value.value, outputCode)
                append("\"", outputCode)
            }
            else -> {
                append(value.toString(), outputCode)
            }
        }
    }

    fun handleIdentifier(node: IdentifierNode) {
        append(node.name, outputCode)
    }

    fun handlePrintStatement(node: PrintStatementNode) {
        append("println(", outputCode)
        evaluate(node.value)
        append(")", outputCode)
        endStatement(outputCode)
        append(config.lineBreaksAfterPrintsRule.apply(), outputCode)
    }

    fun handleDeclaration(node: VariableDeclarationNode) {
        append(node.kind, outputCode)
        append(" ", outputCode)
        append(node.identifier.name, outputCode)
        append(config.spaceAroundColonsRule.apply(), outputCode)
        append(node.identifier.dataType, outputCode)
        append(config.spaceAroundEqualsRule.apply(), outputCode)
        evaluate(node.init)
        endStatement(outputCode)
    }

    fun handleAssignation(node: AssignmentNode) {
        append(node.identifier.name, outputCode)
        append(config.spaceAroundEqualsRule.apply(), outputCode)
        evaluate(node.value)
        endStatement(outputCode)
    }

    fun handleReadEnv(node: ReadEnvNode) {
        append("readEnv(", outputCode)
        append(node.variableName, outputCode)
        append(")", outputCode)
    }

    fun handleReadInput(node: ReadInputNode) {
        append("readInput(", outputCode)
        evaluate(node.message)
        append(")", outputCode)
    }

    fun handleIfNode(node: IfNode) {
        append("if (", outputCode)
        evaluate(node.boolean.bool)
        append(")", outputCode)
        append(config.inlineIfBraceRule.apply(), outputCode)
        indentLevel++
        node.ifStatements.forEach {
            indent(outputCode, config, indentLevel)
            evaluate(it)
        }
        indentLevel--
        indent(outputCode, config, indentLevel)
        append("}\n", outputCode)
    }

    fun handleCompleteIfNode(node: CompleteIfNode) {
        append("if (", outputCode)
        evaluate(node.ifNode.boolean.bool)
        append(")", outputCode)
        append(config.inlineIfBraceRule.apply(), outputCode)
        indentLevel++
        node.ifNode.ifStatements.forEach {
            indent(outputCode, config, indentLevel)
            evaluate(it)
        }
        indentLevel--
        indent(outputCode, config, indentLevel)
        append("}", outputCode)

        val elseNode = node.elseNode
        if (elseNode == null) {
            append("\n", outputCode)
            return
        }

        append(" else", outputCode)
        append(config.inlineIfBraceRule.apply(), outputCode)
        indentLevel++
        elseNode.elseStatements.forEach {
            indent(outputCode, config, indentLevel)
            evaluate(it)
        }
        indentLevel--
        indent(outputCode, config, indentLevel)
        append("}\n", outputCode)
    }
}

private fun append(
    string: String,
    outputCode: StringBuilder,
) {
    outputCode.append(string)
}

private fun indent(
    outputCode: StringBuilder,
    config: FormatterConfig,
    level: Int,
) {
    repeat(level) {
        outputCode.append(config.indentRule.apply())
    }
}

private fun endStatement(outputCode: StringBuilder) {
    outputCode.append(";\n")
}

private fun openExpression(outputCode: StringBuilder) {
    outputCode.append("(")
}

private fun closeExpression(outputCode: StringBuilder) {
    outputCode.append(")")
}
