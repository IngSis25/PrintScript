package main.kotlin.parser

import ASTNode

data class IdentifierNode(
    val name: String, // nombre de variable o función
) : ASTNode
