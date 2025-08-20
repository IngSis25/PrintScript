package parser

import ASTNode
import main.kotlin.parser.IdentifierNode

data class VariableDeclarationNode(
    val identifier: IdentifierNode,
    val varType: String?,
    val value: ASTNode?,
) : ASTNode

// declara una variable nueva, valor y tipo opcionales
