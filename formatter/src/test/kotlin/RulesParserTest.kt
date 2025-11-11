package org.example.formatter

import rules.RulesParser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RulesParserTest {
    @Test
    fun testParseEmptyString() {
        val parser = RulesParser()
        val result = parser.parse("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseEmptyJson() {
        val parser = RulesParser()
        val result = parser.parse("{}")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseWithOurFormat() {
        val parser = RulesParser()
        val json =
            """
            {
                "space_before_colon": true,
                "space_after_colon": true,
                "newline_after_println": 2
            }
            """.trimIndent()
        val result = parser.parse(json)
        assertTrue(result.containsKey("space_before_colon"))
        assertTrue(result.containsKey("space_after_colon"))
        assertTrue(result.containsKey("newline_after_println"))
    }

    @Test
    fun testParseWithTCKFormat() {
        val parser = RulesParser()
        val json =
            """
            {
                "enforce-spacing-around-equals": true,
                "enforce-spacing-after-colon-in-declaration": true,
                "enforce-spacing-before-colon-in-declaration": true,
                "line-breaks-after-println": 2,
                "if-brace-below-line": true,
                "if-brace-same-line": false,
                "indent-inside-if": 4,
                "mandatory-single-space-separation": true
            }
            """.trimIndent()
        val result = parser.parse(json)
        assertTrue(result.containsKey("space_around_equals"))
        assertTrue(result.containsKey("space_after_colon"))
        assertTrue(result.containsKey("space_before_colon"))
        assertTrue(result.containsKey("newline_after_println"))
        assertTrue(result.containsKey("new_line_for_if_brace"))
        assertTrue(result.containsKey("same_line_for_if_brace"))
        assertTrue(result.containsKey("number_of_spaces_indentation"))
        assertTrue(result.containsKey("single_space_separation"))
    }

    @Test
    fun testParseWithMixedFormat() {
        val parser = RulesParser()
        val json =
            """
            {
                "space_before_colon": true,
                "enforce-spacing-around-equals": true,
                "line-breaks-after-println": 2
            }
            """.trimIndent()
        val result = parser.parse(json)
        assertTrue(result.containsKey("space_before_colon"))
        assertTrue(result.containsKey("space_around_equals"))
        assertTrue(result.containsKey("newline_after_println"))
    }

    @Test
    fun testParseIgnoresUnknownKeys() {
        val parser = RulesParser()
        val json =
            """
            {
                "space_before_colon": true,
                "unknown_key": true,
                "another_unknown": false
            }
            """.trimIndent()
        val result = parser.parse(json)
        assertTrue(result.containsKey("space_before_colon"))
        assertFalse(result.containsKey("unknown_key"))
        assertFalse(result.containsKey("another_unknown"))
    }
}
