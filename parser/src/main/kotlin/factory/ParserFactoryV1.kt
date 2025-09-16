package factory

import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import rules.RuleMatcher

class ParserFactoryV1 : ParserFactory {
    override fun create(): DefaultParser {
        val ruleMatcher = RuleMatcher(ConfiguredRules.V1)
        return DefaultParser(ruleMatcher)
    }
}
