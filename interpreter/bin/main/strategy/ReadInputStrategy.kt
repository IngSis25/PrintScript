package org.example.strategy

import org.example.astnode.expressionNodes.ReadInputNode
import org.example.util.Services

val readInputStrategy =
    Strategy<ReadInputNode> { services: Services, node: ReadInputNode ->
        // Evaluar el mensaje (puede ser una expresión)
        val message = services.visit(services, node.message)

        // Por ahora, simplemente devolvemos el mensaje como string
        // En una implementación completa, esto debería leer de la entrada estándar
        message.toString()
    }
