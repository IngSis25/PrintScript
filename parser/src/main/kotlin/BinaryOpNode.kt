package main.kotlin.parser

data class BinaryOpNode(
    val left: ASTNode,
    val operator: String,
    val right: ASTNode,
): ASTNode