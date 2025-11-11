package org.example.strategy

import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue

val literalStrategy =
    Strategy<LiteralNode> { _, node ->
        when (val value = node.value) {
            is LiteralValue.StringValue -> value.value
            is LiteralValue.NumberValue -> value.value.toDouble()
            is LiteralValue.BooleanValue -> value.value
            is LiteralValue.NullValue -> null
            is LiteralValue.PromiseValue -> throw UnsupportedOperationException(
                "PromiseValue not yet supported in interpreter",
            )
        }
    }
