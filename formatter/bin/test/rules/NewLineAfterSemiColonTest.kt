package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class NewLineAfterSemiColonTest {
    @Test
    fun testApplyRuleWithSemicolon() {
        val rule = NewLineAfterSemiColon()
        val input = "let x: number = 5;"
        val result = rule.applyRule(input)
        assertEquals("let x: number = 5;\n", result)
    }

    @Test
    fun testApplyRuleWithMultipleSemicolons() {
        val rule = NewLineAfterSemiColon()
        val input = "let x = 5; let y = 10;"
        val result = rule.applyRule(input)
        assertEquals("let x = 5;\n let y = 10;\n", result)
    }

    @Test
    fun testApplyRuleWithNoSemicolon() {
        val rule = NewLineAfterSemiColon()
        val input = "let x = 5"
        val result = rule.applyRule(input)
        assertEquals("let x = 5", result)
    }
}
