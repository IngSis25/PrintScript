package parser.matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult

interface Matcher<T> {
    fun match(tokens: List<Token>, pos: Int) : ParseResult<T>?
}

//para implementar las reglas de gramatica y no implementar cada regla por separado
//Matcher<T> generico?