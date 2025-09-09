package main.kotlin.analyzer

import org.example.ast.ASTNode
import org.example.ast.AssignmentNode
import org.example.ast.IdentifierNode
import org.example.ast.VariableDeclarationNode

/**
 * Rule for validating identifier format (camelCase, snake_case, etc.)
 */
class IdentifierFormatRule(
    private val config: IdentifierFormatConfig,
) : AnalysisRule {
    override fun analyze(
        node: ASTNode,
        symbolTable: SymbolTable,
        position: SourcePosition,
    ): List<Diagnostic> {
        if (!config.enabled) return emptyList()

        val diagnostics = mutableListOf<Diagnostic>()

        when (node) {
            is IdentifierNode -> {
                if (!isValidFormat(node.name, config.format)) {
                    diagnostics.add(
                        Diagnostic(
                            code = "INVALID_IDENTIFIER_FORMAT",
                            message =
                                "Identifier '${node.name}' does not follow ${config.format.name.lowercase()} " +
                                    "format",
                            severity = DiagnosticSeverity.ERROR,
                            position = position,
                            suggestions = listOf("Consider renaming to: ${suggestValidName(node.name, config.format)}"),
                        ),
                    )
                }
            }
            is VariableDeclarationNode -> {
                // Analyze the identifier in variable declarations
                if (!isValidFormat(node.identifier.name, config.format)) {
                    diagnostics.add(
                        Diagnostic(
                            code = "INVALID_IDENTIFIER_FORMAT",
                            message = "Variable name '${node.identifier.name}' does not follow ${config.format.name
                                .lowercase()} format",
                            severity = DiagnosticSeverity.ERROR,
                            position = position,
                            suggestions =
                                listOf(
                                    "Consider renaming to: ${suggestValidName(node.identifier.name, config.format)}",
                                ),
                        ),
                    )
                }
            }
            is AssignmentNode -> {
                // Analyze the identifier in assignments
                if (!isValidFormat(node.identifier.name, config.format)) {
                    diagnostics.add(
                        Diagnostic(
                            code = "INVALID_IDENTIFIER_FORMAT",
                            message = "Variable name '${node.identifier.name}' does not follow ${config.format.name
                                .lowercase()} format",
                            severity = DiagnosticSeverity.ERROR,
                            position = position,
                            suggestions =
                                listOf(
                                    "Consider renaming to: ${suggestValidName(node.identifier.name, config.format)}",
                                ),
                        ),
                    )
                }
            }
        }

        return diagnostics
    }

    private fun isValidFormat(
        identifier: String,
        format: IdentifierFormat,
    ): Boolean =
        when (format) {
            IdentifierFormat.CAMEL_CASE -> isValidCamelCase(identifier)
            IdentifierFormat.SNAKE_CASE -> isValidSnakeCase(identifier)
            IdentifierFormat.PASCAL_CASE -> isValidPascalCase(identifier)
        }

    private fun isValidCamelCase(identifier: String): Boolean {
        // camelCase: starts with lowercase letter, followed by letters and numbers
        return identifier.matches(Regex("^[a-z][a-zA-Z0-9]*$"))
    }

    private fun isValidSnakeCase(identifier: String): Boolean {
        // snake_case: lowercase letters, numbers, and underscores, no consecutive underscores
        return identifier.matches(Regex("^[a-z][a-z0-9]*(_[a-z0-9]+)*$"))
    }

    private fun isValidPascalCase(identifier: String): Boolean {
        // PascalCase: starts with uppercase letter, followed by letters and numbers
        return identifier.matches(Regex("^[A-Z][a-zA-Z0-9]*$"))
    }

    private fun suggestValidName(
        identifier: String,
        format: IdentifierFormat,
    ): String =
        when (format) {
            IdentifierFormat.CAMEL_CASE -> toCamelCase(identifier)
            IdentifierFormat.SNAKE_CASE -> toSnakeCase(identifier)
            IdentifierFormat.PASCAL_CASE -> toPascalCase(identifier)
        }

    private fun toCamelCase(identifier: String): String =
        when {
            identifier.contains("_") -> {
                // Convert snake_case to camelCase
                identifier
                    .split("_")
                    .mapIndexed { index, part ->
                        if (index == 0) {
                            part.lowercase()
                        } else {
                            part.lowercase().replaceFirstChar { it.uppercase() }
                        }
                    }.joinToString("")
            }
            identifier.first().isUpperCase() -> {
                // Convert PascalCase to camelCase
                identifier.replaceFirstChar { it.lowercase() }
            }
            else -> identifier
        }

    private fun toSnakeCase(identifier: String): String =
        when {
            identifier.contains("_") -> identifier.lowercase()
            else -> {
                // Convert camelCase or PascalCase to snake_case
                identifier.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
            }
        }

    private fun toPascalCase(identifier: String): String =
        when {
            identifier.contains("_") -> {
                // Convert snake_case to PascalCase
                identifier
                    .split("_")
                    .joinToString("") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
            }
            identifier.first().isLowerCase() -> {
                // Convert camelCase to PascalCase
                identifier.replaceFirstChar { it.uppercase() }
            }
            else -> identifier
        }
}
