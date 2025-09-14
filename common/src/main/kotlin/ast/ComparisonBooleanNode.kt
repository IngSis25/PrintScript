package org.example.ast

data class ComparisonBooleanNode(
    val left: ASTNode,
    val operator: String,
    val right: ASTNode,
) : BooleanNode()
