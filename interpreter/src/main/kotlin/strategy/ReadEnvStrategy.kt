package org.example.strategy

import ast.ReadEnvNode

val readEnvStrategy =
    Strategy<ReadEnvNode> { _, node ->
        val envVarName = node.envVarName
        val envValue = System.getenv(envVarName)
        
        if (envValue == null) {
            throw RuntimeException("Environment variable '$envVarName' not found")
        }
        
        envValue
    }