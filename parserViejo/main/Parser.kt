package main.kotlin.parser

import main.kotlin.lexer.Token
import org.example.ast.ASTNode

interface Parser {
    fun parse(tokens: List<Token>): ParseResult<ASTNode>?
}
