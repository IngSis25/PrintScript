package main.kotlin.analyzer

import org.example.ast.ASTNode
import org.example.ast.PrintlnNode

/**
 * Rule for validating println function restrictions
 */
class PrintlnRestrictionRule(
    private val config: PrintlnRestrictionConfig,
) : AnalysisRule {
    override fun analyze(
        node: ASTNode,
        symbolTable: SymbolTable,
        position: SourcePosition,
    ): List<Diagnostic> {
        if (!config.enabled || !config.allowOnlyIdentifiersAndLiterals) return emptyList()

        val diagnostics = mutableListOf<Diagnostic>()

        if (node is PrintlnNode) {
            if (TypeOf.isComplexExpression(node.value)) {
                diagnostics.add(
                    Diagnostic(
                        code = "PRINTLN_COMPLEX_EXPRESSION",
                        message =
                            "println() function can only be called with identifiers or literals, " +
                                "not complex expressions",
                        severity = DiagnosticSeverity.ERROR,
                        position = position,
                        suggestions =
                            listOf(
                                "Extract the expression to a variable first",
                                "Use a simple identifier or literal instead",
                            ),
                    ),
                )
            }
        }

        return diagnostics
    }
}
