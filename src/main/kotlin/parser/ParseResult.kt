package main.kotlin.parser

data class ParseResult<T>(
    val node: T,
    val nextPosition: Int
)
