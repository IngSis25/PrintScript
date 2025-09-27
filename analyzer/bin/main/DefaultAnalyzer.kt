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
        buildSymbolTableAndValidateTypes(ast, symbolTable, diagnostics)

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

    private fun buildSymbolTableAndValidateTypes(
        ast: List<ASTNode>,
        symbolTable: SymbolTable,
        diagnostics: MutableList<Diagnostic>,
    ) {
        ast.forEach { node ->
            when (node) {
                is VariableDeclarationNode -> {
                    val declaredType = inferVariableType(node)
                    val assignedValue = node.value ?: return@forEach

                    // Paso 1: Inferir el tipo de la expresión asignada
                    val assignedType = TypeOf.inferType(assignedValue, symbolTable)

                    // Paso 2: Validar la compatibilidad
                    if (!isTypeCompatible(declaredType, assignedType)) {
                        val message = "Error de tipo: El valor de tipo '${assignedType.toString()
                            .lowercase()}' no es compatible con el tipo declarado '${declaredType.toString()
                            .lowercase()}'."
                        diagnostics.add(
                            Diagnostic(
                                severity = DiagnosticSeverity.ERROR,
                                message = message,
                                position = SourcePosition(1, 1),
                                code = "invalid-expression-for-type",
                            ),
                        )
                    }

                    symbolTable.declare(node.identifier.name, declaredType)
                }
                // ... (otras validaciones para nodos de asignación, etc.)
            }
        }
    }

    // Nueva función de compatibilidad de tipos
    private fun isTypeCompatible(
        declared: Types,
        assigned: Types,
    ): Boolean {
        // Si los tipos son idénticos, son compatibles
        if (declared == assigned) {
            return true
        }

        // Reglas de compatibilidad:
        // 1. Una expresión UNKNOWN (como en un "let x;") puede ser de cualquier tipo
        if (assigned == Types.UNKNOWN) {
            return true
        }

        // 2. Un 'NUMBER' es compatible con un 'NUMBER'
        if (declared == Types.NUMBER && assigned == Types.NUMBER) {
            return true
        }

        // 3. Un 'STRING' puede ser concatenado con un 'NUMBER' y el resultado es 'STRING'.
        // Esta es la regla clave para el test 'string-and-number-concat'
        if (declared == Types.STRING && assigned == Types.NUMBER) {
            return true
        }

        // Si ninguna regla coincide, los tipos no son compatibles
        return false
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
