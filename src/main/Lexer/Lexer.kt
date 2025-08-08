package main.Lexer

import Token

interface Lexer{
    fun tokenize(sourceCode: String): List<Token>
}