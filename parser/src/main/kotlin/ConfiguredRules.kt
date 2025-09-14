import builders.AssignmentBuilder
import builders.ExpressionBuilder
import builders.PrintBuilder
import builders.VariableDeclarationBuilder
import parser.rules.AssignmentRule
import parser.rules.ParserRule
import rules.ExpressionRule
import rules.PrintlnRule
import rules.VariableDeclarationRule

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

//    val V1_1: List<ParserRule> =
//        V1 + listOf(
//            IfRule(IfNodeBuilder()),
//            ElseRule(ElseNodeBuilder()),
//            BlockRule(
//                BlockBuilder()),
//            ConstRule(ConstBuilder()),
//
//        )
}
