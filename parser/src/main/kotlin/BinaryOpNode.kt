package main.kotlin.parser

import ASTNode

data class BinaryOpNode(
    val left: ASTNode,
    val operator: String,
    val right: ASTNode,
) : ASTNode
