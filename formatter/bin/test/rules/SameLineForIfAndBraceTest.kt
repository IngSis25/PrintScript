package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class SameLineForIfAndBraceTest {
    @Test
    fun testApplyRuleWithIfAndBraceOnNewLine() {
        val rule = SameLineForIfAndBrace()
        val input = "if (true)\n{"
        val result = rule.applyRule(input)
        assertEquals("if (true) {", result)
    }

    @Test
    fun testApplyRuleWithIfAndBraceWithSpaces() {
        val rule = SameLineForIfAndBrace()
        val input = "if (true)\n  {"
        val result = rule.applyRule(input)
        assertEquals("if (true) {", result)
    }

    @Test
    fun testApplyRuleWithIfAlreadyOnSameLine() {
        val rule = SameLineForIfAndBrace()
        val input = "if (true) {"
        val result = rule.applyRule(input)
        assertEquals("if (true) {", result)
    }

    @Test
    fun testApplyRuleWithComplexCondition() {
        val rule = SameLineForIfAndBrace()
        val input = "if (x > 5)\n{"
        val result = rule.applyRule(input)
        assertEquals("if (x > 5) {", result)
    }
}
