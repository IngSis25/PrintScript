package main.kotlin.lexer

import org.example.Lexer.Token

interface Lexer {
    fun tokenize(sourceCode: String): List<Token>
}
