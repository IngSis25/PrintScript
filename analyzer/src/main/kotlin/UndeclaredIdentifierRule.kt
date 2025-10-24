package main.kotlin.analyzer

import org.example.ast.*

class UndeclaredIdentifierRule : AnalysisRule {
    override fun analyze(
        node: ASTNode,
        symbolTable: SymbolTable,
        position: SourcePosition,
    ): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()

        fun checkExpr(e: ASTNode?) {
            when (e) {
                is IdentifierNode -> {
                    if (!symbolTable.contains(e.name)) {
                        diags +=
                            Diagnostic(
                                code = "UNDECLARED_IDENTIFIER",
                                message = "Undeclared identifier '${e.name}'",
                                severity = DiagnosticSeverity.ERROR,
                                position = position,
                            )
                    }
                }
                is BinaryOpNode -> {
                    checkExpr(e.left)
                    checkExpr(e.right)
                }
            }
        }

        when (node) {
            is PrintlnNode -> checkExpr(node.value)
            is AssignmentNode -> {
                if (!symbolTable.contains(node.identifier.name)) {
                    diags +=
                        Diagnostic(
                            code = "ASSIGN_UNDECLARED",
                            message = "Assignment to undeclared variable '${node.identifier.name}'",
                            severity = DiagnosticSeverity.ERROR,
                            position = position,
                        )
                }
                checkExpr(node.value)
            }
            is VariableDeclarationNode -> checkExpr(node.value)
        }

        return diags
    }
}
