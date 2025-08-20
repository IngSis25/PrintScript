package main.kotlin.parser

import ASTNode
import main.kotlin.lexer.TokenType

data class LiteralNode(
    val value: String,
    val literalType: TokenType, // sino separar en literal number, literal string, etc
) : ASTNode
