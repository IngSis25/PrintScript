package org.example.ast

data class IfNode(
    val condition: ASTNode, // ahora puede ser cualquier expresión booleana
    val thenBlock: ASTNode,
    val elseBlock: ASTNode? = null,
) : ASTNode
