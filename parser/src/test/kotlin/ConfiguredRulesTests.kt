package test.parserTest

import builders.*
import main.kotlin.parser.ConfiguredRules
import kotlin.test.*

class ConfiguredRulesTests {
    @Test
    fun v1_should_be_immutable() {
        val rules1 = ConfiguredRules.V1
        val rules2 = ConfiguredRules.V1

        // Should be the same instance (object)
        assertSame(rules1, rules2)
    }
}
