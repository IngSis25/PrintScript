package org.example.strategy

import org.example.astnode.expressionNodes.IdentifierNode
import org.example.util.Services

val identifierStrategy =
    Strategy<IdentifierNode> { services: Services, node: IdentifierNode ->
        val varName = node.name

        // Buscamos en el contexto
        if (!services.context.containsKey(varName)) {
            throw RuntimeException("Variable $varName no declarada")
        }

        // Devolvemos el valor asociado (puede ser null si nunca se inicializó)
        services.context[varName]
    }
