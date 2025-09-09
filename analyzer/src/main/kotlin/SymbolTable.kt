package main.kotlin.analyzer

/**
 * Represents a type in the type system
 */
sealed class Types {
    object NUMBER : Types()

    object STRING : Types()

    object UNKNOWN : Types()

    // Fácil agregar nuevos tipos:
    object BOOLEAN : Types()

    object ARRAY : Types()

    override fun toString(): String = this::class.simpleName ?: "UNKNOWN"
}

/**
 * Information about a symbol in the symbol table
 */
data class SymbolInfo(
    val name: String,
    val type: Types,
    val isMutable: Boolean = true,
    val declaredAt: SourcePosition? = null,
)

/**
 * Symbol table for tracking variables and their types
 */
class SymbolTable(
    private val parent: SymbolTable? = null,
) {
    private val symbols = mutableMapOf<String, SymbolInfo>()

    /**
     * Declares a new symbol in the current scope
     */
    fun declare(
        name: String,
        type: Types,
        isMutable: Boolean = true,
        position: SourcePosition? = null,
    ): SymbolTable {
        // Don't allow redeclaration in the same scope
        if (!symbols.containsKey(name)) {
            symbols[name] = SymbolInfo(name, type, isMutable, position)
        }
        return this
    }

    /**
     * Checks if a symbol exists in the current scope or any parent scope
     */
    fun contains(name: String): Boolean = symbols.containsKey(name) || parent?.contains(name) == true

    /**
     * Checks if a symbol exists only in the current scope (not inherited)
     */
    fun containsInCurrentScope(name: String): Boolean = symbols.containsKey(name)

    /**
     * Gets the type of a symbol
     */
    fun get(name: String): Types? = symbols[name]?.type ?: parent?.get(name)

    /**
     * Gets detailed information about a symbol
     */
    fun getSymbolInfo(name: String): SymbolInfo? = symbols[name] ?: parent?.getSymbolInfo(name)

    /**
     * Gets all symbols including inherited ones (child symbols override parent ones)
     */
    fun getAllSymbols(): Map<String, SymbolInfo> {
        val allSymbols = mutableMapOf<String, SymbolInfo>()

        // Add parent symbols first
        parent?.getAllSymbols()?.let { allSymbols.putAll(it) }

        // Override with current scope symbols
        allSymbols.putAll(symbols)

        return allSymbols
    }

    /**
     * Creates a new child scope
     */
    fun createChildScope(): SymbolTable = SymbolTable(this)

    /**
     * Gets the current scope depth
     */
    fun getDepth(): Int = (parent?.getDepth() ?: 0) + 1

    /**
     * Data class for source location information
     */
    data class SourceLocation(
        val line: Int,
        val column: Int,
    )
}
