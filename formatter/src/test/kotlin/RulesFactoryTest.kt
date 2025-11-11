package org.example.formatter

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RulesFactoryTest {
    @Test
    fun testGetRulesV10WithEmptyJson() {
        val factory = RulesFactory()
        val rules = factory.getRules("{}", "1.0")
        // Debe tener las reglas no configurables
        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun testGetRulesV10WithAllRules() {
        val factory = RulesFactory()
        val json =
            """
            {
                "space_before_colon": true,
                "space_after_colon": true,
                "newline_after_println": 2,
                "newline_before_println": 1,
                "space_around_equals": true,
                "no_space_around_equals": false,
                "single_space_separation": true
            }
            """.trimIndent()
        val rules = factory.getRules(json, "1.0")
        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun testGetRulesV10WithFalseRules() {
        val factory = RulesFactory()
        val json =
            """
            {
                "space_before_colon": false,
                "space_around_equals": false
            }
            """.trimIndent()
        val rules = factory.getRules(json, "1.0")
        // Las reglas con false no se agregan, pero las no configurables sí
        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun testGetRulesV11WithAllRules() {
        val factory = RulesFactory()
        val json =
            """
            {
                "space_before_colon": true,
                "space_after_colon": true,
                "newline_after_println": 2,
                "newline_before_println": 1,
                "space_around_equals": true,
                "no_space_around_equals": false,
                "number_of_spaces_indentation": 2,
                "same_line_for_if_brace": true,
                "same_line_for_else_brace": true,
                "new_line_for_if_brace": false,
                "single_space_separation": true
            }
            """.trimIndent()
        val rules = factory.getRules(json, "1.1")
        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun testGetRulesV11WithIndentation() {
        val factory = RulesFactory()
        val json =
            """
            {
                "number_of_spaces_indentation": 4
            }
            """.trimIndent()
        val rules = factory.getRules(json, "1.1")
        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun testGetRulesWithUnsupportedVersion() {
        val factory = RulesFactory()
        assertFailsWith<IllegalStateException> {
            factory.getRules("{}", "2.0")
        }
    }

    @Test
    fun testGetRulesWithTCKFormat() {
        val factory = RulesFactory()
        val json =
            """
            {
                "enforce-spacing-around-equals": true,
                "enforce-spacing-after-colon-in-declaration": true,
                "enforce-spacing-before-colon-in-declaration": true,
                "line-breaks-after-println": 2
            }
            """.trimIndent()
        val rules = factory.getRules(json, "1.0")
        assertTrue(rules.isNotEmpty())
    }
}
