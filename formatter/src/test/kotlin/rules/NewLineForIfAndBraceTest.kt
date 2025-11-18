package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class NewLineForIfAndBraceTest {
    @Test
    fun testApplyRuleWithIfOnSameLine() {
        val rule = NewLineForIfAndBrace()
        val input = "if (true) {"
        val result = rule.applyRule(input)
        assertEquals("if (true)\n{", result)
    }

    @Test
    fun testApplyRuleWithIfAndSpaces() {
        val rule = NewLineForIfAndBrace()
        val input = "if (true)   {"
        val result = rule.applyRule(input)
        assertEquals("if (true)\n{", result)
    }

    @Test
    fun testApplyRuleWithIfAlreadyOnNewLine() {
        val rule = NewLineForIfAndBrace()
        val input = "if (true)\n{"
        val result = rule.applyRule(input)
        assertEquals("if (true)\n{", result)
    }

    @Test
    fun testApplyRuleWithComplexCondition() {
        val rule = NewLineForIfAndBrace()
        val input = "if (x > 5 && y < 10) {"
        val result = rule.applyRule(input)
        assertEquals("if (x > 5 && y < 10)\n{", result)
    }
}
