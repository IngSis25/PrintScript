package org.example

import main.kotlin.parser.ASTNode

interface Interpreter {
    fun execute(input: List<ASTNode>) //no devuelve nada, el resultado es el efecto
}