package org.example.strategy

import org.example.astnode.ASTNode
import org.example.builder.StrategyProviderBuilder

/**
 * A provider of [Strategy] to use in the [Interpreter].
 */
sealed interface StrategyProvider {
    /**
     * Gets the [Strategy] for the given [ASTNode] type. Método para pedir la receta correcta para un nodo.
     */
    infix fun <T> getStrategyFor(node: T): Strategy<T>? where T : ASTNode
// val s: Strategy<PrintlnNode>? = provider getStrategyFor printlnNode -> forma de uso

    /**
     * Combines this [StrategyProvider] with another [StrategyProvider].
     * Entries in [other] override entries in this provider when keys collide.
     */

    operator fun plus(other: StrategyProvider): StrategyProvider

    /**
     * Returns an iterator over the [Pair] of [Class] and [Strategy] of this [StrategyProvider].
     * Useful for merging providers or debugging registered strategies.
     */
    fun iterator(): Iterator<Pair<Class<out ASTNode>, Strategy<out ASTNode>>>

    companion object {
        /**
         * Creates a new [StrategyProvider] using the builder-style DSL.
         */
        infix fun builder(block: StrategyProviderBuilder.() -> Unit): StrategyProvider =
            StrategyProviderBuilder().apply(block).build()

        /**
         * Creates a concrete implementation from a prebuilt map.
         */
        internal infix fun implementation(
            strategies: Map<Class<out ASTNode>, Strategy<out ASTNode>>,
        ): StrategyProvider = StrategyProviderImplementation(strategies)
    }

    /**
     * Default map-backed implementation.
     */
    private class StrategyProviderImplementation(
        private val strategies: Map<Class<out ASTNode>, Strategy<out ASTNode>>,
    ) : StrategyProvider {
        @Suppress("UNCHECKED_CAST")
        override fun <T> getStrategyFor(node: T): Strategy<T>? where T : ASTNode =
            strategies[node::class.java] as Strategy<T>?

        override fun plus(other: StrategyProvider): StrategyProvider {
            val merged = mutableMapOf<Class<out ASTNode>, Strategy<out ASTNode>>()
            // current entries
            strategies.forEach { (k, v) -> merged[k] = v }
            // other entries (override on collision)
            other.iterator().forEach { (k, v) -> merged[k] = v }
            return StrategyProviderImplementation(merged.toMap())
        }

        override fun iterator(): Iterator<Pair<Class<out ASTNode>, Strategy<out ASTNode>>> =
            strategies.map { it.toPair() }.iterator()
    }
}
