package org.example.strategy

import org.example.Environment
import org.example.astnode.expressionNodes.ReadEnvNode
import org.example.util.Services

val readEnvStrategy =
    Strategy<ReadEnvNode> { services: Services, node: ReadEnvNode ->
        // 1) Buscar primero en el Environment estático
        val fromStatic = Environment.getGlobalVariable(node.variableName)?.toString()

        // 2) Fallback al context si queremos respetar el wiring existente
        val fromContext = services.context[node.variableName]?.toString()

        // 3) Devuelve el valor o string vacío
        fromStatic ?: fromContext ?: ""
    }
