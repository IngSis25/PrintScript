package main.kotlin.parser
import LiteralBooleanRule
import builders.AssignmentBuilder
import builders.BlockBuilder
import builders.BooleanIdentifierBuilder
import builders.ElseNodeBuilder
import builders.ExpressionBuilder
import builders.IfNodeBuilder
import builders.LiteralBooleanBuilder
import builders.PrintBuilder
import builders.VariableDeclarationBuilder
import rules.*
import rules.booleanExpressions.BooleanIdentifierRule

object ConfiguredRules {
    // Versión 1.0 configuración de reglas del parser
    val V1: List<ParserRule> =
        listOf(
            // Order matters: more specific rules first
            PrintlnRule(PrintBuilder()),
            VariableDeclarationRule(VariableDeclarationBuilder()),
            AssignmentRule(AssignmentBuilder()),
            ExpressionRule(ExpressionBuilder()),
        )

    // Función para crear las reglas V1_1 con un parser
    fun createV11Rules(parser: DefaultParser): List<ParserRule> {
        // Crear reglas de expresiones booleanas primero
        val booleanRules =
            listOf(
                LiteralBooleanRule(LiteralBooleanBuilder()),
                BooleanIdentifierRule(BooleanIdentifierBuilder()),
            )

        // Crear un parser especializado solo para condiciones (sin reglas de if/else para evitar recursión)
        val conditionRules = V1 + booleanRules
        val conditionParser = DefaultParser(RuleMatcher(conditionRules as List<ParserRule>))

        // Crear las reglas finales
        val allRules =
            conditionRules +
                listOf(
                    // Reglas para estructuras de control
                    IfRule(IfNodeBuilder(conditionParser)),
                    ElseRule(ElseNodeBuilder(conditionParser)),
                    BlockRule(BlockBuilder(conditionParser), RuleMatcher(conditionRules)),
                    LetRule(VariableDeclarationBuilder()),
                    // ConstRule is already included in V1, no need to add it again
                )

        return allRules
    }
}
