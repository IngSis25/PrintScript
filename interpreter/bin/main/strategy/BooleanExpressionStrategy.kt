package org.example.strategy

import org.example.astnode.expressionNodes.BooleanExpressionNode
import org.example.util.Services

val booleanExpressionStrategy =
    Strategy<BooleanExpressionNode> { services: Services, node: BooleanExpressionNode ->
        // Evaluar la expresión booleana interna
        val result = services.visit(services, node.bool)

        // Asegurarse de que el resultado es un Boolean
        when (result) {
            is Boolean -> result
            else -> throw IllegalArgumentException(
                "BooleanExpressionNode debe evaluar a Boolean, pero obtuvo: ${result?.javaClass?.simpleName}",
            )
        }
    }
