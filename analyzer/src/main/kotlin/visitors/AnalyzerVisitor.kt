package visitors

import main.kotlin.analyzer.SymbolTable
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

interface AnalyzerVisitor : ASTNodeVisitor {
    fun checkWarnings(): VisitorResult

    override fun visit(node: ASTNode): VisitorResult

    fun setSymbolTable(table: SymbolTable) { }
}
