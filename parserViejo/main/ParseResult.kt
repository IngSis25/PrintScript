package main.kotlin.parser

sealed class ParseResult<out T> {
    data class Success<T>(
        val node: T,
        val nextPosition: Int,
    ) : ParseResult<T>()

    data class Failure(
        val message: String,
        val position: Int,
    ) : ParseResult<Nothing>()
}
