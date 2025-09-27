package org.example.strategy

import org.example.ast.IdentifierBooleanNode
import org.example.ast.LiteralBooleanNode

val literalBooleanStrategy =
    Strategy<LiteralBooleanNode> { _, node ->
        node.value
    }

val identifierBooleanStrategy =
    Strategy<IdentifierBooleanNode> { services, node ->
        val value = services.context[node.name]
        when {
            value == null -> throw IllegalArgumentException("Undefined variable: ${node.name}")
            value !is Boolean -> throw IllegalArgumentException(
                "Variable ${node.name} is not a boolean, got: ${value.javaClass.simpleName}",
            )
            else -> value
        }
    }
