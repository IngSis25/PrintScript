package main.kotlin.parser

data class IdentifierNode(
    val name: String             // nombre de variable o función
) : ASTNode
