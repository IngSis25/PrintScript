package org.example.astnode.statamentNode

import lexer.Location
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

interface StatementNode : ASTNode {
    override val type: String
    override val location: Location

    override fun accept(visitor: ASTNodeVisitor): VisitorResult
}
