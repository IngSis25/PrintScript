package main.kotlin.lexer

import org.example.Lexer.Location
import org.example.Lexer.Token
// esto va adevolver un token , desde un "let" y su ubicacion

class TokenResolver(
    private val tokenMap: List<Pair<String, String>>,
) {
    fun getToken(
        component: String,
        location: Location,
    ): Token {
        val tokenType =
            tokenMap
                .firstOrNull { (pattern, _) ->
                    // null si ninguno coincide
                    component.matches(Regex(pattern)) // crea el objeto regex -> REGEX
                }?.second ?: throw Exception(
                "Lexicon Error: " +
                    "No token found for component: $component at $location",
            )

        return Token(
            type = tokenType,
            value = component.replace("\"", "").replace("'", ""),
            // sacamos comillas dobles y simples "hola" = hola
            location = location,
        )
    }
}
