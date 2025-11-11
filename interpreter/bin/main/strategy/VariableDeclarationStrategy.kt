package org.example.strategy

import org.example.astnode.statamentNode.VariableDeclarationNode
import org.example.util.Services

val variableDeclarationStrategy =
    Strategy<VariableDeclarationNode> { services: Services, node: VariableDeclarationNode ->
        val varName = node.identifier.name

        // Ya existe en el contexto → error
        if (services.context.containsKey(varName)) {
            throw RuntimeException("Variable $varName ya declarada")
        }

        // Evaluamos valor inicial
        val initValue = services.visit(services, node.init)

        // Construimos nuevo contexto con la variable agregada
        val newContext = services.context.toMutableMap()
        newContext[varName] = initValue

        // Devolvemos un nuevo Services con ese contexto
        services.copy(context = newContext.toMap())
    }
