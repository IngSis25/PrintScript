package main.kotlin.analyzer

import org.example.iterator.PrintScriptIterator

/**
 * Interface for static code analyzers
 */
interface Analyzer {
    val version: String
    val nodeIterator: PrintScriptIterator<org.example.astnode.ASTNode>

    /**
     * Analyzes a list of AST nodes and returns analysis results
     * @param ast The abstract syntax tree to analyze
     * @param config Configuration for the analyzer
     * @return Analysis result containing diagnostics
     */
    fun analyze(config: AnalyzerConfig): AnalysisResult
}
