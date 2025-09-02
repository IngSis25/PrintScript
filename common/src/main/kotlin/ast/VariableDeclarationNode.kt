package org.example.ast

data class VariableDeclarationNode(
    val identifier: IdentifierNode,
    val varType: String?,
    val value: ASTNode?,
) : ASTNode

// declara una variable nueva, valor y tipo opcionales
