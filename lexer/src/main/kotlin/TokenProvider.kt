package main.kotlin.lexer

class TokenProvider

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
