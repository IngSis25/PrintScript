package org.example.astnode

import org.example.Lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

interface ASTNode {
    val type: String
    val location: Location

    fun accept(visitor: ASTNodeVisitor): VisitorResult
}
