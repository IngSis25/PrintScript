package org.example.strategy

import main.kotlin.parser.LiteralNode

val literalStrategy =
    Strategy<LiteralNode> { _, node ->
        val raw = node.value

        when {
            // string con comillas
            raw.length >= 2 &&
                (
                    (raw.first() == '"' && raw.last() == '"') ||
                        (raw.first() == '\'' && raw.last() == '\'')
                ) ->
                raw.substring(1, raw.length - 1)

            // número
            raw.toDoubleOrNull() != null -> raw.toDouble()

            // default: string crudo
            else -> raw
        }
    }
