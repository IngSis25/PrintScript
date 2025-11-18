package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class SpaceAfterAndBeforeOperatorsTest {
    @Test
    fun testApplyRuleWithAddition() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "x+y"
        val result = rule.applyRule(input)
        assertEquals("x + y", result)
    }

    @Test
    fun testApplyRuleWithSubtraction() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "x-y"
        val result = rule.applyRule(input)
        assertEquals("x - y", result)
    }

    @Test
    fun testApplyRuleWithMultiplication() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "x*y"
        val result = rule.applyRule(input)
        assertEquals("x * y", result)
    }

    @Test
    fun testApplyRuleWithDivision() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "x/y"
        val result = rule.applyRule(input)
        assertEquals("x / y", result)
    }

    @Test
    fun testApplyRuleWithMultipleSpaces() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "x  +  y"
        val result = rule.applyRule(input)
        assertEquals("x + y", result)
    }

    @Test
    fun testApplyRuleWithNoSpaces() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "x+y*z"
        val result = rule.applyRule(input)
        assertEquals("x + y * z", result)
    }

    @Test
    fun testApplyRuleWithMixedOperators() {
        val rule = SpaceAfterAndBeforeOperators()
        val input = "a+b-c*d/e"
        val result = rule.applyRule(input)
        assertEquals("a + b - c * d / e", result)
    }
}
