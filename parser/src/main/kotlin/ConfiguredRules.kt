import builders.ExpressionBuilder
import builders.PrintBuilder
import builders.VariableDeclarationBuilder
import parser.rules.ParserRule
import parser.rules.VariableDeclarationRule
import rules.ExpressionRule
import rules.PrintlnRule

object ConfiguredRules {
    // Versión 1.0 configuración de reglas del parser
    val V1: List<ParserRule> =
        listOf(
            // Order matters: more specific rules first
            PrintlnRule(PrintBuilder()),
            VariableDeclarationRule(VariableDeclarationBuilder()),
            ExpressionRule(ExpressionBuilder()),
        )
}
