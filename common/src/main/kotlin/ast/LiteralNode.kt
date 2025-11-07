package org.example.ast

data class LiteralNode(
    val value: String,
    // sino separar en literal number, literal string, etc
) : ASTNode
