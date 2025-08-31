package org.example

import org.example.ast.ASTNode

interface Interpreter {
    fun execute(input: List<ASTNode>) // no devuelve nada, el resultado es el efecto
}
