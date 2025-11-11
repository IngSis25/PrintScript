package main.kotlin.analyzer

import org.example.ast.VariableDeclarationNode
import org.example.astnode.ASTNode

object SymbolTableBuilder {
    fun build(nodes: MutableList<org.example.astnode.ASTNode>): SymbolTable {
        val table = SymbolTable()
        nodes.forEach { node ->
            when (node) {
                is VariableDeclarationNode -> {
                    val inferredType = inferVariableType(node, table)
                    val position =
                        node.location?.let {
                            SourcePosition(it.line, it.column)
                        } ?: SourcePosition(1, 1)
                    table.declare(
                        name = node.identifier.name,
                        type = inferredType,
                        isMutable = true,
                        position = position,
                    )
                }
            }
        }
        return table
    }

    private fun inferVariableType(
        node: VariableDeclarationNode,
        table: SymbolTable,
    ): Types =
        when {
            node.varType != null -> TypeOf.stringToType(node.varType!!)
            node.value != null -> TypeOf.inferType(node.value!! as ASTNode, table)
            else -> Types.UNKNOWN
        }
}
