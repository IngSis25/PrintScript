package org.example.strategy

import org.example.astnode.statamentNode.PrintStatementNode
import org.example.util.Services

val printStatementStrategy =
    Strategy<PrintStatementNode> { services: Services, node: PrintStatementNode ->
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
        services.output write "$formattedValue\n"
        services // Devolver el Services actual para preservar el contexto
    }
