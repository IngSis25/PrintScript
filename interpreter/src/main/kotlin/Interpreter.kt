package org.example

import org.example.astnode.ASTNode
import org.example.input.Input
import org.example.iterator.PrintScriptIterator
import org.example.output.Output
import org.example.strategy.StrategyProvider
import org.example.util.Services

class Interpreter(
    private val output: Output,
    private val input: Input,
    private val provider: StrategyProvider,
) {
    private var context = mutableMapOf<String, Any?>()

    // Ejecuta el programa iterando sobre todos los nodos AST
    fun interpret(astNodeIterator: PrintScriptIterator<ASTNode>) {
        // Función helper para crear Services con el contexto actual
        fun createServices(): Services =
            Services(
                context = context.toMap(),
                output = output,
                input = input,
                visit = { s: Services, node: ASTNode ->
                    val strategy =
                        (provider getStrategyFor node)
                            ?: error("Unsupported node: ${node::class.simpleName}")
                    val result = strategy.visit(s, node)

                    // Si la strategy devuelve un nuevo Services, actualizar el contexto
                    if (result is Services) {
                        context = result.context.toMutableMap()
                    }
                    result
                },
            )

        // Iterar sobre todos los nodos AST del programa
        while (astNodeIterator.hasNext()) {
            val node = astNodeIterator.next()
            if (node != null) {
                val services = createServices()
                val result = services.visit(services, node)

                // Actualizar contexto si la strategy devolvió un nuevo Services
                if (result is Services) {
                    context = result.context.toMutableMap()
                }
            }
        }
    }
}
