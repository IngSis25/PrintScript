package org.example.strategy
import main.kotlin.parser.IdentifierNode
import org.example.util.Services

// Nota: IdentifierNode está en default package en tu parser.

val identifierStrategy = Strategy<IdentifierNode> { services: Services, node: IdentifierNode ->
    val varName = node.name

    // buscamos en el contexto
    if (!services.context.containsKey(varName)) {
        throw RuntimeException("Variable $varName no declarada")
    }

    services.context[varName] // devolvemos el valor asociado (puede ser null si nunca se inicializó)
}

