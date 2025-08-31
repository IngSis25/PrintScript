package org.example.ast

data class IdentifierNode(
    val name: String, // nombre de variable o función
) : ASTNode
