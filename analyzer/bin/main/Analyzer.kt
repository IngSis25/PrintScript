package main.kotlin.analyzer

import org.example.ast.ASTNode

/**
 * Interface for static code analyzers
 */
interface Analyzer {
    /**
     * Analyzes a list of AST nodes and returns analysis results
     * @param ast The abstract syntax tree to analyze
     * @param config Configuration for the analyzer
     * @return Analysis result containing diagnostics
     */
    fun analyze(
        ast: List<ASTNode>,
        config: AnalyzerConfig,
    ): AnalysisResult
}
