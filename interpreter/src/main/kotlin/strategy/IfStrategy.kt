package org.example.strategy

import org.example.astnode.statamentNode.CompleteIfNode
import org.example.astnode.statamentNode.IfNode

val ifStrategy =
    Strategy<IfNode> { services, node ->
        // Evaluar la condición booleana
        val condition =
            services.visit(services, node.boolean) as? Boolean
                ?: throw IllegalArgumentException(
                    "La condición del if debe ser un Boolean, pero obtuvo: ${node.boolean::class.simpleName}",
                )

        if (condition) {
            // Ejecutar los statements del bloque if
            node.ifStatements.forEach { statement ->
                services.visit(services, statement)
            }
        }
    }

val completeIfStrategy =
    Strategy<CompleteIfNode> { services, node ->
        // Evaluar la condición booleana del if
        val condition =
            services.visit(services, node.ifNode.boolean) as? Boolean
                ?: throw IllegalArgumentException(
                    "La condición del if debe ser un Boolean, pero obtuvo: ${node.ifNode.boolean::class.simpleName}",
                )

        if (condition) {
            // Ejecutar los statements del bloque if
            node.ifNode.ifStatements.forEach { statement ->
                services.visit(services, statement)
            }
        } else {
            // Si hay un bloque else, ejecutarlo
            node.elseNode?.elseStatements?.forEach { statement ->
                services.visit(services, statement)
            }
        }
    }
