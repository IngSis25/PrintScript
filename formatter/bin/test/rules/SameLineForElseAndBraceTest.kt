package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class SameLineForElseAndBraceTest {
    @Test
    fun testApplyRuleWithElseOnNewLine() {
        val rule = SameLineForElseAndBrace()
        val input = "}\nelse {"
        val result = rule.applyRule(input)
        assertEquals("} else {", result)
    }

    @Test
    fun testApplyRuleWithElseAndSpaces() {
        val rule = SameLineForElseAndBrace()
        val input = "}\n  else {"
        val result = rule.applyRule(input)
        assertEquals("} else {", result)
    }

    @Test
    fun testApplyRuleWithElseAlreadyOnSameLine() {
        val rule = SameLineForElseAndBrace()
        val input = "} else {"
        val result = rule.applyRule(input)
        assertEquals("} else {", result)
    }
}
