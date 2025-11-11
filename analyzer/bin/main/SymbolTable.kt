package main.kotlin.analyzer

class SymbolTable(
    private val parent: SymbolTable? = null,
) {
    private val symbols = mutableMapOf<String, SymbolInfo>()

    fun declare(
        name: String,
        type: Types,
        isMutable: Boolean = true,
        position: SourcePosition? = null,
    ): SymbolTable {
        if (!symbols.containsKey(name)) {
            symbols[name] = SymbolInfo(name, type, isMutable, position)
        }
        return this
    }

    fun contains(name: String): Boolean = symbols.containsKey(name) || parent?.contains(name) == true

    fun containsInCurrentScope(name: String): Boolean = symbols.containsKey(name)

    fun isDeclared(name: String): Boolean = contains(name)

    fun getType(name: String): Types? = symbols[name]?.type ?: parent?.getType(name)

    fun get(name: String): Types? = getType(name)

    fun getSymbolInfo(name: String): SymbolInfo? = symbols[name] ?: parent?.getSymbolInfo(name)

    fun updateType(
        name: String,
        newType: Types,
    ) {
        symbols[name]?.type = newType
    }

    fun getAllSymbols(): Map<String, SymbolInfo> {
        val allSymbols = mutableMapOf<String, SymbolInfo>()

        parent?.getAllSymbols()?.let { allSymbols.putAll(it) }

        allSymbols.putAll(symbols)

        return allSymbols
    }

    fun createChildScope(): SymbolTable = SymbolTable(this)

    fun getDepth(): Int = (parent?.getDepth() ?: 0) + 1

    data class SymbolInfo(
        val name: String,
        var type: Types,
        val isMutable: Boolean = true,
        val declaredAt: SourcePosition? = null,
    ) {
        val position: SourcePosition? get() = declaredAt
    }
}
