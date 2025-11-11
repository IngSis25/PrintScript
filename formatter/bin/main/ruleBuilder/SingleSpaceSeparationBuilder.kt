package ruleBuilder

import rules.Rule
import rules.SingleSpaceSeparation

class SingleSpaceSeparationBuilder : RuleBuilder {
    override fun buildRule(
        ruleName: String,
        value: String,
    ): Rule = SingleSpaceSeparation()
}
