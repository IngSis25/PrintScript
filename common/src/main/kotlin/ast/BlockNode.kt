package org.example.ast

data class BlockNode(
    val statements: List<ASTNode>,
) : ASTNode
