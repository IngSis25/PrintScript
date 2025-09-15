package org.example.strategy
import com.printscript.interpreter.strategy.Strategy
import org.example.ast.AssignmentNode
import org.example.util.Services

val assignmentStrategy =
    Strategy<AssignmentNode> { services: Services, node: AssignmentNode ->
        val varName = node.identifier.name

        when {
            // 1) si la variable no existe → error
            !services.context.containsKey(varName) ->
                throw RuntimeException("Variable $varName no declarada")

            // 2)  evaluamos RHS y devolvemos NUEVO Services con contexto actualizado
            else -> {
                val newValue = services.visit(services, node.value)
                val newContext = services.context.toMutableMap().apply { this[varName] = newValue }
                services.copy(context = newContext.toMap())
            }
        }
    }
