package rules

import rules.ParserRule

interface BooleanExpressionRule : ParserRule {
    // Cada implementación debe definir cómo matchea tokens y construye su AST
}
