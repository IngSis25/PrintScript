package rules

import kotlin.test.Test
import kotlin.test.assertEquals

class NumberOfSpacesIndentationTest {
    @Test
    fun testApplyRuleWithTwoSpaces() {
        val rule = NumberOfSpacesIndentation(2)
        val input = "if (true) {\nprintln(\"test\");\n}"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n  println(\"test\");\n}", result)
    }

    @Test
    fun testApplyRuleWithFourSpaces() {
        val rule = NumberOfSpacesIndentation(4)
        val input = "if (true) {\nprintln(\"test\");\n}"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n    println(\"test\");\n}", result)
    }

    @Test
    fun testApplyRuleWithNestedBraces() {
        val rule = NumberOfSpacesIndentation(2)
        val input = "if (true) {\nif (false) {\nprintln(\"test\");\n}\n}"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n  if (false) {\n    println(\"test\");\n  }\n}", result)
    }

    @Test
    fun testApplyRuleWithEmptyLines() {
        val rule = NumberOfSpacesIndentation(2)
        val input = "if (true) {\n\nprintln(\"test\");\n}"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n\n  println(\"test\");\n}", result)
    }

    @Test
    fun testApplyRuleWithClosingBrace() {
        val rule = NumberOfSpacesIndentation(2)
        val input = "if (true) {\n  println(\"test\");\n}"
        val result = rule.applyRule(input)
        // El closing brace debe tener un nivel menos de indentación
        assertEquals("if (true) {\n  println(\"test\");\n}", result)
    }

    @Test
    fun testApplyRuleWithMultipleStatements() {
        val rule = NumberOfSpacesIndentation(2)
        val input = "if (true) {\nlet x = 5;\nprintln(x);\n}"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\n  let x = 5;\n  println(x);\n}", result)
    }

    @Test
    fun testApplyRuleWithZeroSpaces() {
        val rule = NumberOfSpacesIndentation(0)
        val input = "if (true) {\nprintln(\"test\");\n}"
        val result = rule.applyRule(input)
        assertEquals("if (true) {\nprintln(\"test\");\n}", result)
    }
}
