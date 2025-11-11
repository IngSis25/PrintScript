package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class NewLineAfterBraceTest {
    @Test
    fun testApplyRuleWithOpeningBraceNoNewline() {
        val rule = NewLineAfterBrace()
        val input = "if (true) {"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n", result)
    }

    @Test
    fun testApplyRuleWithClosingBraceNoNewline() {
        val rule = NewLineAfterBrace()
        val input = "}"
        val result = rule.applyRule(input)
        assertEquals("}\n", result)
    }

    @Test
    fun testApplyRuleWithBothBraces() {
        val rule = NewLineAfterBrace()
        val input = "if (true) { }"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n }\n", result)
    }

    @Test
    fun testApplyRuleWithExistingNewline() {
        val rule = NewLineAfterBrace()
        val input = "if (true) {\n}"
        val result = rule.applyRule(input)
        // Ya tiene newline, no debe duplicarlo
        assertEquals("if (true) {\n}\n", result)
    }

    @Test
    fun testApplyRuleWithMultipleBraces() {
        val rule = NewLineAfterBrace()
        val input = "{ { } }"
        val result = rule.applyRule(input)
        assertEquals("{\n {\n }\n }\n", result)
    }
}
