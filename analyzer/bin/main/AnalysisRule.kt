package main.kotlin.analyzer

import org.example.ast.ASTNode

/**
 * Base interface for all analysis rules
 */
interface AnalysisRule {
    /**
     * Analyzes an AST node and returns a list of diagnostics
     * @param node The AST node to analyze
     * @param symbolTable The current symbol table
     * @param position The position of the node in the source code
     * @return List of diagnostics found during analysis
     */
    fun analyze(
        node: ASTNode,
        symbolTable: SymbolTable,
        position: SourcePosition,
    ): List<Diagnostic>
}
