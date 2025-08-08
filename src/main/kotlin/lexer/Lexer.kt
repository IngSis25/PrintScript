package main.kotlin.lexer

interface Lexer{
    fun tokenize(sourceCode: String): List<Token>
}