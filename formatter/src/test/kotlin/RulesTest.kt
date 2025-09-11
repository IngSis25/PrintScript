package formatter

import io.printscript.rule.LineBreaksBeforePrints
import io.printscript.rule.SpaceAroundColons
import io.printscript.rule.SpaceAroundEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class RulesTest {
    @Test
    fun `space around equals - with spaces`() {
        // Arrange
        val rule = SpaceAroundEquals(true)
        // Act
        val result = rule.apply()
        // Assert
        assertEquals(" = ", result)
    }

    @Test
    fun `space around equals - without spaces`() {
        val rule = SpaceAroundEquals(false)
        val result = rule.apply()
        assertEquals("=", result)
    }

    @Test
    fun `space around colons - before=false after=true`() {
        val rule = SpaceAroundColons(spaceBefore = false, spaceAfter = true)
        val result = rule.apply()
        assertEquals(": ", result)
    }

    @Test
    fun `space around colons - before=true after=false`() {
        val rule = SpaceAroundColons(spaceBefore = true, spaceAfter = false)
        val result = rule.apply()
        assertEquals(" :", result)
    }

    @Test
    fun `space around colons - before=true after=true`() {
        val rule = SpaceAroundColons(spaceBefore = true, spaceAfter = true)
        val result = rule.apply()
        assertEquals(" : ", result)
    }

    @Test
    fun `space around colons - before=false after=false`() {
        val rule = SpaceAroundColons(spaceBefore = false, spaceAfter = false)
        val result = rule.apply()
        assertEquals(":", result)
    }

    @Test
    fun `line breaks before prints - zero`() {
        val rule = LineBreaksBeforePrints(0)
        val result = rule.apply()
        assertEquals("", result)
    }

    @Test
    fun `line breaks before prints - one`() {
        val rule = LineBreaksBeforePrints(1)
        val result = rule.apply()
        assertEquals("\n", result)
    }

    @Test
    fun `line breaks before prints - two`() {
        val rule = LineBreaksBeforePrints(2)
        val result = rule.apply()
        assertEquals("\n\n", result)
    }
}
