package main.kotlin.lexer

import lexer.TokenRule

class TokenProvider ( private val rules: List<TokenRule>){
fun rules() : List<TokenRule> {
    return rules
}
companion object {
 fun fromMap(tokenMap : Map<String , TokenType>) : TokenProvider {
     val listaToken = tokenMap.map{ (regex, type) ->
         val regexConG = if (regex.startsWith("\\G")) regex else "\\G$regex"
         val compiled = Regex(regexConG) // para sepan Regex es de kotlin , es string a objeto Regex.
         TokenRule(pattern = compiled, type = type)
     }
     return TokenProvider(listaToken)
 }

}}


/*
Flujo:

Recibir el mapa.
Recorrer cada entrada (regex, tipo).
Asegurarte de que la regex esté anclada con \G (para que matchee desde la posición actual del lexer).
Compilar la regex (Regex(...)).
Crear un lexer.TokenRule con esa regex y el tipo.
Guardar todo en una lista.
Devolver el main.kotlin.lexer.TokenProvider que guarda esa lista.


1- "Cuando alguien cree un main.kotlin.lexer.TokenProvider, me tiene que pasar una lista de lexer.TokenRule".
 */




