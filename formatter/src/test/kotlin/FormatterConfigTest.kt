package formatter

import org.example.formatter.config.FormatterConfig
import org.example.formatter.rule.LineBreaksBeforePrints
import org.example.formatter.rule.SpaceAroundColons
import org.example.formatter.rule.SpaceAroundEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FormatterConfigTest {
    // AAA: Arrange, Act, Assert

    @Test
    fun `valid config creates expected rules`() {
        // Arrange
        val config =
            FormatterConfig(
                lineBreaksBeforePrints = 1,
                spaceAroundEquals = true,
                spaceBeforeColon = false,
                spaceAfterColon = true,
            )

        // Act
        val lbRule = config.lineBreaksBeforePrintsRule()
        val eqRule = config.spaceAroundEqualsRule()
        val colonRule = config.spaceAroundColonsRule()

        // Assert
        assertEquals("\n", (lbRule as LineBreaksBeforePrints).apply())
        assertEquals(" = ", (eqRule as SpaceAroundEquals).apply())
        assertEquals(": ", (colonRule as SpaceAroundColons).apply())
    }

    @Test
    fun `invalid lineBreaksBeforePrints throws`() {
        // Arrange
        // Act + Assert
        assertFailsWith<IllegalArgumentException> {
            FormatterConfig(
                lineBreaksBeforePrints = 3,
                spaceAroundEquals = false,
                spaceBeforeColon = false,
                spaceAfterColon = false,
            )
        }
    }
}
