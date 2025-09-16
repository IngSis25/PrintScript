package factory

import ConfiguredRules
import DefaultParser
import rules.RuleMatcher

class ParserFactoryV1 : ParserFactory {
    override fun create(): DefaultParser {
        val ruleMatcher = RuleMatcher(ConfiguredRules.V1)
        return DefaultParser(ruleMatcher)
    }
}
