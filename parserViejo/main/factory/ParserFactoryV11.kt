package factory

import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import rules.RuleMatcher

class ParserFactoryV11 : ParserFactory {
    override fun create(): DefaultParser {
        // Crear el parser base primero
        val baseParser = DefaultParser(RuleMatcher(ConfiguredRules.V1))

        // Ahora crear las reglas V1_1 con el parser
        val v11Rules = ConfiguredRules.createV11Rules(baseParser)
        val v11RuleMatcher = RuleMatcher(v11Rules)

        // Retornar un nuevo parser con las reglas V1_1
        return DefaultParser(v11RuleMatcher)
    }
}
