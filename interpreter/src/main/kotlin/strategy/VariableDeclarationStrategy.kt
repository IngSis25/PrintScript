package org.example.strategy

import org.example.util.Services
import parser.VariableDeclarationNode



val variableDeclarationStrategy = Strategy<VariableDeclarationNode> { services: Services, node: VariableDeclarationNode ->
    val varName = node.identifier.name

    // ya existe en el contexto → error
    if (services.context.containsKey(varName)) {
        throw RuntimeException("Variable $varName ya declarada")
    }

    // evaluamos valor inicial si lo hay
    val initValue = node.value?.let { services.visit(services, it) }

    // construimos nuevo contexto con la variable agregada
    val newContext = services.context.toMutableMap()
    newContext[varName] = initValue

    // devolvemos un nuevo Services con ese contexto
    services.copy(context = newContext.toMap())
}
