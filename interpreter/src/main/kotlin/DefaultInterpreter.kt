package org.example
import com.printscript.interpreter.output.Output
import com.printscript.interpreter.strategy.StrategyProvider
import org.example.ast.ASTNode
import org.example.util.Services

class DefaultInterpreter(private val output: Output, private val provider: StrategyProvider) {
    private val context = mutableMapOf<String, Any?>()

    private val services = Services(
        context = context, // pasa como Map
        output = output,
        visit = { s: Services, node: ASTNode ->
            val strategy = (provider getStrategyFor node)
                ?: error("Unsupported node: ${node::class.simpleName}")
            strategy.visit(s, node)
        }
    )
    //Ejecuta el programa
    fun interpret(root: ASTNode) {
        val strategy = (provider getStrategyFor root)
            ?: error("Unsupported node: ${root::class.simpleName}")
        strategy.visit(services, root)
    }
}
