package main.kotlin.analyzer

import org.example.astnode.ASTNode
import org.example.astnode.statamentNode.VariableDeclarationNode

object SymbolTableBuilder {
    fun build(nodes: MutableList<ASTNode>): SymbolTable {
        val table = SymbolTable()
        nodes.forEach { node ->
            when (node) {
                is VariableDeclarationNode -> {
                    val inferredType = inferVariableType(node, table)
                    val position =
                        node.location?.let {
                            SourcePosition(it.getLine(), it.getColumn())
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
            node.identifier.dataType.isNotEmpty() -> TypeOf.stringToType(node.identifier.dataType)
            else -> TypeOf.inferType(node.init as ASTNode, table)
        }
}
