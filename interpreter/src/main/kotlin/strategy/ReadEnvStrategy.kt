package org.example.strategy

import ast.ReadEnvNode
import org.example.Environment
import org.example.util.Services

val readEnvStrategy =
    Strategy<ReadEnvNode> { services: Services, node: ReadEnvNode ->
        // 1) Buscá primero en tu Environment.kt
        val fromStatic = Environment.getGlobalVariable(node.envVarName)?.toString()

        // 2) Fallback al context si querés respetar el wiring existente
        val fromContext = services.context[node.envVarName]?.toString()

        // 3) Devuelve el valor o vacío
        fromStatic ?: fromContext ?: ""
    }
