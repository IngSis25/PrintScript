package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class PreserveEqualsFromOriginalTest {
    @Test
    fun testApplyRuleWithSpacesInOriginal() {
        FormattingContext.originalLine = "let x = 5;"
        val rule = PreserveEqualsFromOriginal()
        val input = "let x=5;"
        val result = rule.applyRule(input)
        assertEquals("let x = 5;", result)
    }

    @Test
    fun testApplyRuleWithNoSpacesInOriginal() {
        FormattingContext.originalLine = "let x=5;"
        val rule = PreserveEqualsFromOriginal()
        val input = "let x = 5;"
        val result = rule.applyRule(input)
        assertEquals("let x=5;", result)
    }

    @Test
    fun testApplyRuleWithNoOriginalLine() {
        FormattingContext.originalLine = null
        val rule = PreserveEqualsFromOriginal()
        val input = "let x = 5;"
        val result = rule.applyRule(input)
        assertEquals("let x = 5;", result)
    }

    @Test
    fun testApplyRuleWithNoEqualsInOriginal() {
        FormattingContext.originalLine = "println(\"test\");"
        val rule = PreserveEqualsFromOriginal()
        val input = "println(\"test\");"
        val result = rule.applyRule(input)
        assertEquals("println(\"test\");", result)
    }

    @Test
    fun testApplyRuleWithAssignment() {
        FormattingContext.originalLine = "x = 5;"
        val rule = PreserveEqualsFromOriginal()
        val input = "x=5;"
        val result = rule.applyRule(input)
        assertEquals("x = 5;", result)
    }

    @Test
    fun testApplyRuleWithNoSpacesAroundEqualsInOriginal() {
        FormattingContext.originalLine = "x=5"
        val rule = PreserveEqualsFromOriginal()
        val input = "x = 5"
        val result = rule.applyRule(input)
        assertEquals("x=5", result)
    }
}
