package main.kotlin.parser

import ASTNode
import main.kotlin.lexer.Token

interface Parser {
    fun parse(tokens: List<Token>): ParseResult<ASTNode>?
}
