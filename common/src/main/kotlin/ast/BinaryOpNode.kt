package org.example.ast

data class BinaryOpNode(
    val left: ASTNode,
    val operator: String,
    val right: ASTNode,
) : ASTNode
