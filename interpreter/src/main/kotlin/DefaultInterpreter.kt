package org.example

import org.example.ast.ASTNode
import org.example.output.Output
import org.example.strategy.StrategyProvider
import org.example.util.Services

class DefaultInterpreter(private val output: Output, private val provider: StrategyProvider) {
    private var context = mutableMapOf<String, Any?>()

    //Ejecuta el programa
    fun interpret(root: ASTNode) {
        val services = Services(
            context = context.toMap(),
            output = output,
            visit = { s: Services, node: ASTNode ->
                val strategy = (provider getStrategyFor node)
                    ?: error("Unsupported node: ${node::class.simpleName}")
                val result = strategy.visit(s, node)
                
                // Si la strategy devuelve un nuevo Services, actualizar el contexto
                if (result is Services) {
                    context = result.context.toMutableMap()
                }
                result
            }
        )
        
        val strategy = (provider getStrategyFor root)
            ?: error("Unsupported node: ${root::class.simpleName}")
        val result = strategy.visit(services, root)
        
        // Actualizar contexto si la strategy devolvió un nuevo Services
        if (result is Services) {
            context = result.context.toMutableMap()
        }
    }
}
