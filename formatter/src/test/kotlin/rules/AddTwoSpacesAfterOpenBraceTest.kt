package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class AddTwoSpacesAfterOpenBraceTest {
    @Test
    fun testApplyRuleWithContentAfterBrace() {
        val rule = AddTwoSpacesAfterOpenBrace()
        val input = "if (true){\nprintln(\"test\");"
        val result = rule.applyRule(input)
        assertEquals("if (true){\n  println(\"test\");", result)
    }

    @Test
    fun testApplyRuleWithBraceOnNewLine() {
        val rule = AddTwoSpacesAfterOpenBrace()
        val input = "if (true)\n{\nprintln(\"test\");"
        val result = rule.applyRule(input)
        assertEquals("if (true)\n{\n  println(\"test\");", result)
    }
}
