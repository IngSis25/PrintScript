package org.example.astnode.astNodeVisitor

import org.example.astnode.expressionNodes.LiteralValue

sealed class VisitorResult {
    data class LiteralValueResult(
        val value: LiteralValue,
    ) : VisitorResult()

    data class StringResult(
        val value: String,
    ) : VisitorResult() {
        override fun toString(): String = value
    }

    data class MapResult(
        val value: Map<String, Any>,
    ) : VisitorResult()

    data class ListResult(
        val value: List<String>,
    ) : VisitorResult()

    data object Empty : VisitorResult()
}
