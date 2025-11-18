package rules

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OnlyOneSpacePermitedTest {
    @Test
    fun testApplyRuleWithMultipleSpaces() {
        val rule = OnlyOneSpacePermited()
        val input = "let  x  =  5;"
        val result = rule.applyRule(input)
        assertEquals("let x = 5;", result)
    }

    @Test
    fun testApplyRulePreservesIndentation() {
        val rule = OnlyOneSpacePermited()
        val input = "  let  x  =  5;"
        val result = rule.applyRule(input)
        // Debe preservar la indentación inicial
        assertTrue(result.startsWith("  "))
        assertEquals("  let x = 5;", result)
    }

    @Test
    fun testApplyRuleWithSingleSpace() {
        val rule = OnlyOneSpacePermited()
        val input = "let x = 5;"
        val result = rule.applyRule(input)
        assertEquals("let x = 5;", result)
    }

    @Test
    fun testApplyRuleWithMultipleLines() {
        val rule = OnlyOneSpacePermited()
        val input = "let  x  =  5;\nprintln(  x  );"
        val result = rule.applyRule(input)
        assertEquals("let x = 5;\nprintln( x );", result)
    }

    @Test
    fun testApplyRuleDoesNotAffectLineStart() {
        val rule = OnlyOneSpacePermited()
        val input = "    let  x  =  5;"
        val result = rule.applyRule(input)
        // La indentación al inicio debe preservarse
        assertTrue(result.startsWith("    "))
        assertEquals("    let x = 5;", result)
    }
}
