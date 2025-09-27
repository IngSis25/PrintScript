package org.example.strategy

import org.example.ast.ASTNode
import org.example.ast.BlockNode
import org.example.ast.IfNode

val ifStrategy =
    Strategy<IfNode> { services, node ->
        // Evaluar la condición
        val condition = services.visit(services, node.condition)

        when {
            condition !is Boolean -> throw IllegalArgumentException(
                "Condition must be a boolean, got: ${condition?.javaClass?.simpleName}",
            )
            condition -> {
                // Ejecutar el bloque then
                handleBranch(services, node.thenBlock)
            }
            node.elseBlock != null -> {
                // Ejecutar el bloque else si existe
                handleBranch(services, node.elseBlock!!)
            }
            // Si condition es false y no hay else, no hacer nada
        }
    }

private fun handleBranch(
    services: org.example.util.Services,
    branch: ASTNode,
) {
    when (branch) {
        is BlockNode -> {
            // Ejecutar cada statement en el bloque
            branch.statements.forEach { statement ->
                services.visit(services, statement)
            }
        }
        else -> {
            // Si no es un bloque, ejecutar directamente
            services.visit(services, branch)
        }
    }
}
