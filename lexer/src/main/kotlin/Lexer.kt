package main.kotlin.lexer

import org.example.Lexer.Location
import org.example.Lexer.Token
import org.example.iterator.PrintScriptIterator
import java.io.Reader
import java.util.LinkedList
import java.util.Queue

class Lexer(
    private val lexicon: TokenResolver,
    private val reader: Reader,
) : PrintScriptIterator<Token> {
    private val currentTokens: Queue<Token> = LinkedList()
    private val position: Position = Position(1, 1)
    private var finished = false

    override fun hasNext(): Boolean {
        if (currentTokens.isNotEmpty()) return true
        if (finished) return false

        // Lexeamos TODO lo que queda del reader UNA sola vez
        lexAll()
        finished = true
        return currentTokens.isNotEmpty()
    }

    override fun next(): Token {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return currentTokens.remove()
    }

    override fun peek(): Token? {
        if (!hasNext()) return null
        return currentTokens.peek()
    }

    /**
     * Lee TODO el contenido que queda en el reader
     * y lo tokeniza usando tu pipeline original.
     */
    private fun lexAll() {
        val statement = StringBuilder()
        var intChar = reader.read()

        while (intChar != -1) {
            val char = intChar.toChar()
            statement.append(char)
            intChar = reader.read()
        }

        if (statement.isNotEmpty()) {
            tokenizeStatement(statement.toString())
        }
    }

    private fun tokenizeStatement(statement: String) {
        val components = splitIgnoringLiterals(statement)

        for (component in components) {
            when {
                // Si el componente tiene saltos de línea, los procesamos uno por uno
                component.contains('\n') -> {
                    component.forEach { ch ->
                        if (ch == '\n') {
                            handleNewLine()
                        } else if (ch.isWhitespace()) {
                            position.column++
                        }
                    }
                    continue
                }

                // Espacios/tabs: avanzan columnas pero no generan tokens
                component.isBlank() -> {
                    position.column += component.length
                    continue
                }

                else -> {
                    tokenizeComponent(component)
                }
            }
        }
    }

    private fun tokenizeComponent(component: String) {
        val subComponents = splitComponent(component)
        for (subComponent in subComponents) {
            val token =
                try {
                    lexicon.getToken(subComponent, Location(position.line, position.column))
                } catch (e: Exception) {
                    throw Exception(e.message ?: "Unknown error")
                }

            currentTokens.add(token)
            position.column += subComponent.length
        }
    }

    private fun handleNewLine() {
        position.line++
        position.column = 1
    }

    private fun splitIgnoringLiterals(input: String): List<String> {
        val regex =
            Regex(
                "[a-zA-Z_][a-zA-Z0-9_]*" + // identificadores
                    "|:|;|=" + // símbolos simples
                    "|[0-9]+(?:\\.[0-9]+)?" + // números
                    "|\".*?\"|'.*?'" + // strings
                    "|\\n" + // newline
                    "|\\s+" + // espacios/tabs
                    "|[^\\s]", // cualquier otro símbolo suelto
            )
        return regex.findAll(input).map { it.value }.toList()
    }

    /**
     * También reciclado: si alguna vez tenés componentes mezclados,
     * esto los vuelve a partir con el mismo criterio.
     */
    private fun splitComponent(component: String): List<String> {
        val regex =
            Regex(
                "[a-zA-Z_][a-zA-Z0-9_]*" +
                    "|:|;|=" +
                    "|[0-9]+(?:\\.[0-9]+)?" +
                    "|\".*?\"|'.*?'" +
                    "|\\S",
            )
        return regex.findAll(component).map { it.value }.toList()
    }

    /**
     * Versión corregida: usamos ESTE lexer, no creamos uno nuevo con el mismo reader.
     */
    fun collectAllTokens(): LexerResult {
        val result = LexerResult()

        try {
            while (hasNext()) {
                result.addToken(next())
            }
        } catch (e: Exception) {
            result.addError(e.message ?: "Unknown error")
        }

        return result
    }
}
