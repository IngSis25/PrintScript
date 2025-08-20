package main.kotlin.lexer

import lexer.TokenRule

class DefaultLexer(
    private val tokenProvider: TokenProvider,
) : Lexer {
    override fun tokenize(sourceCode: String): List<Token> = tokenizeRecursively(sourceCode, 0, 1, 1, emptyList())

    private fun tokenizeRecursively(
        sourceCode: String,
        index: Int,
        line: Int,
        column: Int,
        tokens: List<Token>,
    ): List<Token> {
        if (index >= sourceCode.length) {
            return tokens
        }
        val match =
            firstToMatch(sourceCode, index)
                ?: throw LexicalException("No se encontró un token válido en la posición $index del código fuente.")

        val (rule, tokenValue) = match
        val newTokens = if (rule.ignore) tokens else tokens + Token(rule.type, tokenValue, line, column)
        val (newLine, newColumn) = updateLineAndColumn(tokenValue, line, column)

        return tokenizeRecursively(sourceCode, index + tokenValue.length, newLine, newColumn, newTokens)
    }

    private fun updateLineAndColumn(
        tokenValue: String,
        line: Int,
        column: Int,
    ): Pair<Int, Int> {
        val lines = tokenValue.split("\n")
        return if (lines.size == 1) {
            Pair(line, column + tokenValue.length) // no hay saltos de linea
        } else {
            val newLine = line + lines.size - 1
            val newColumn = lines.last().length + 1 // columna despues del ultimo salto de linea
            Pair(newLine, newColumn)
        }
    }

    private fun firstToMatch(
        sourceCode: String,
        index: Int,
    ): Pair<TokenRule, String>? {
        val rules = tokenProvider.rules()
        for (rule in rules) {
            val matchResult = rule.pattern.find(sourceCode, index)

            if (matchResult != null && matchResult.range.first == index) {
                val tokenValue = matchResult.value
                return Pair(rule, tokenValue)
            }
        }
        return null
    }
}
