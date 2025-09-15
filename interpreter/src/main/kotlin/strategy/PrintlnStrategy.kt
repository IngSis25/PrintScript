package org.example.strategy
import org.example.ast.PrintlnNode
import org.example.util.Services

val printlnStrategy =
    Strategy<PrintlnNode> { services: Services, node: PrintlnNode ->
        val value = services.visit(services, node.value)
        val formattedValue =
            when (value) {
                is Double -> {
                    // Si es un número entero, mostrarlo sin decimales
                    if (value == value.toLong().toDouble()) {
                        value.toLong().toString()
                    } else {
                        value.toString()
                    }
                }

                else -> value.toString()
            }
        services.output write formattedValue
        services // ← Devolver el Services actual para preservar el contexto
    }
