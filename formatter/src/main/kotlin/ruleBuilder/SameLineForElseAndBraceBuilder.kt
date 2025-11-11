package ruleBuilder

import rules.Rule
import rules.SameLineForElseAndBrace

class SameLineForElseAndBraceBuilder : RuleBuilder {
    override fun buildRule(
        ruleName: String,
        value: String,
    ): Rule = SameLineForElseAndBrace()
}
