package main.kotlin.parser

data class ParseResult(
    val node: ASTNode,
    val nextPosition: Int
)
