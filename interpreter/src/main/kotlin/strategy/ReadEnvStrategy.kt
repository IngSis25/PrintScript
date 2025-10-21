package org.example.strategy

import ast.ReadEnvNode
import org.example.util.Services

val readEnvStrategy =
    Strategy<ReadEnvNode> { services: Services, node: ReadEnvNode ->
        val envValue = services.environment[node.envVarName]

        envValue ?: services.context[node.envVarName]?.toString() ?: ""
    }
