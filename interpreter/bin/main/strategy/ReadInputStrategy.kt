package org.example.strategy

import org.example.astnode.expressionNodes.ReadInputNode

val readInputStrategy =
    Strategy<ReadInputNode> { services, node ->
        // Evaluar el mensaje (puede ser una expresión como "Name:" o una variable)
        val message = services.visit(services, node.message)

        // Imprimir el mensaje (prompt) sin salto de línea
        services.output write message.toString()

        // Leer de la entrada usando el mensaje como prompt
        val inputValue = services.input read message.toString()

        // Aplicar transformación para intentar convertir a número o boolean si es posible
        transform(inputValue)
    }

private val transform = { value: String ->
    try {
        value.toBooleanStrict()
    } catch (_: IllegalArgumentException) {
        try {
            // Convertir a Double para mantener consistencia con el resto del sistema
            // que usa Double para todos los números
            value.toDouble()
        } catch (_: NumberFormatException) {
            value
        }
    }
}
