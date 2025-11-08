package org.example.astnode.astNodeVisitor

import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult

interface ASTNodeVisitor {
    fun visit(node: ASTNode): VisitorResult
}
