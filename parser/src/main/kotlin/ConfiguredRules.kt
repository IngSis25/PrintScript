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

//    val parserV1_1 by lazy {
//        DefaultParser(RuleMatcher(V1_1_rules))
//    }
//
//    val V1_1_rules: List<ParserRule> by lazy {
//        V1 +
//            listOf(
//                IfRule(IfNodeBuilder(parserV1_1)),
//                ElseRule(ElseNodeBuilder(parserV1_1)),
//                BlockRule(BlockBuilder(parserV1_1)),
//                ConstRule(ConstBuilder(parserV1_1)),
//            )
//    }
}
