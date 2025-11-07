package org.example.astnode.expressionNodes

import lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class LiteralNode(
    override val type: String,
    override val location: Location,
    val value: LiteralValue,
) : ExpressionNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)

    override fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String = value.getType()

    override fun toString(): String = value.toString()
}

sealed class LiteralValue {
    abstract fun getType(): String

    data class StringValue(
        val value: String,
    ) : LiteralValue() {
        override fun getType(): String = "String"

        override fun toString(): String = value
    }

    data class NumberValue(
        val value: Number,
    ) : LiteralValue() {
        override fun getType(): String = "Number"

        override fun toString(): String = value.toString()
    }

    data class BooleanValue(
        val value: Boolean,
    ) : LiteralValue() {
        override fun getType(): String = "boolean"

        override fun toString(): String = value.toString()
    }

    // para readEnv y readInput
    data object NullValue : LiteralValue() {
        override fun getType(): String = "null"

        override fun toString(): String = "null"
    }

    // para ReadEnv y readInput
    data object PromiseValue : LiteralValue() {
        override fun getType(): String = "Promise"

        override fun toString(): String = "Promise"
    }
}
