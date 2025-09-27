package org.example.ast

import org.example.TokenType

data class LiteralNode(
    val value: String,
    val literalType: TokenType, // sino separar en literal number, literal string, etc
) : ASTNode
