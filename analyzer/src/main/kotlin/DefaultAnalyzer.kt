package main.kotlin.analyzer

import org.example.ast.ASTNode
import org.example.ast.VariableDeclarationNode

/**
 * Default implementation of the static code analyzer
 */
class DefaultAnalyzer : Analyzer {
    override fun analyze(
        ast: List<ASTNode>,
        config: AnalyzerConfig,
    ): AnalysisResult {
        val diagnostics = mutableListOf<Diagnostic>()
        val symbolTable = SymbolTable()
        val rules = createRules(config)

        // First pass: collect variable declarations to build symbol table
        buildSymbolTable(ast, symbolTable)

        // Second pass: analyze with rules
        analyzeWithRules(ast, rules, symbolTable, diagnostics, config)

        // Limit diagnostics based on maxErrors configuration
        val limitedDiagnostics =
            if (config.maxErrors > 0) {
                diagnostics.take(config.maxErrors)
            } else {
                diagnostics
            }

        return AnalysisResult(limitedDiagnostics)
    }

    private fun createRules(config: AnalyzerConfig): List<AnalysisRule> =
        listOf(
            IdentifierFormatRule(config.identifierFormat),
            PrintlnRestrictionRule(config.printlnRestrictions),
        )

    private fun buildSymbolTable(
        ast: List<ASTNode>,
        symbolTable: SymbolTable,
    ) {
        ast.forEach { node ->
            when (node) {
                is VariableDeclarationNode -> {
                    val type = inferVariableType(node)
                    symbolTable.declare(
                        name = node.identifier.name,
                        type = type,
                        isMutable = true,
                        position = SourcePosition(1, 1), // TODO: Get actual position from tokens
                    )
                }
            }
        }
    }

    private fun inferVariableType(node: VariableDeclarationNode): Types =
        when {
            node.varType != null -> {
                when (node.varType!!.uppercase()) {
                    "NUMBER" -> Types.NUMBER
                    "STRING" -> Types.STRING
                    "BOOLEAN" -> Types.BOOLEAN
                    "ARRAY" -> Types.ARRAY
                    else -> Types.UNKNOWN
                }
            }
            node.value != null -> TypeOf.inferType(node.value!!, SymbolTable())
            else -> Types.UNKNOWN
        }

    private fun analyzeWithRules(
        ast: List<ASTNode>,
        rules: List<AnalysisRule>,
        symbolTable: SymbolTable,
        diagnostics: MutableList<Diagnostic>,
        config: AnalyzerConfig,
    ) {
        ast.forEach { node ->
            val position = SourcePosition(1, 1) // TODO: Get actual position from tokens

            rules.forEach { rule ->
                val ruleDiagnostics = rule.analyze(node, symbolTable, position)
                diagnostics.addAll(ruleDiagnostics)

                // Stop if we've reached the max error limit
                if (config.maxErrors > 0 && diagnostics.size >= config.maxErrors) {
                    return
                }
            }
        }
    }
}
