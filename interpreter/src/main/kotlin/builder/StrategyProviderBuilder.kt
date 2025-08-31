package org.example.builder

import ASTNode
import AssignmentNode
import main.kotlin.parser.LiteralNode
import org.example.strategy.Strategy
import org.example.strategy.StrategyProvider
import org.example.strategy.assignmentStrategy

/**
 * Builder usado para armar un StrategyProvider con un DSL.
 */
class StrategyProviderBuilder {
    private val strategies = mutableMapOf<Class<out ASTNode>, Strategy<out ASTNode>>()

    /** Inline + reified → permite usar: this addStrategy printlnStrategy */
    inline infix fun <reified T : ASTNode> addStrategy(strategy: Strategy<T>) {
        addStrategy(T::class.java, strategy)
    } //magia

    /** Versión explícita → permite usar la clase manualmente */
    fun <T : ASTNode> addStrategy(
        type: Class<T>,
        strategy: Strategy<T>,
    ) {
        strategies[type] = strategy
    }

    /** Construye el StrategyProvider con todas las strategies registradas */
    fun build(): StrategyProvider = StrategyProvider implementation strategies

    fun hola() {
        addStrategy(LiteralNode::class.java, assignmentStrategy)
        strategies[LiteralNode::class.java] = assignmentStrategy
        addStrategy(assignmentStrategy)
    }
}
