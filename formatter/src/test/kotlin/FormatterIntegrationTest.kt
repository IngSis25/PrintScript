package formatter

import org.example.LiteralString
import org.example.ast.LiteralNode
import org.example.ast.PrintlnNode
import org.example.formatter.Formatter
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterIntegrationTest {
    // AAA: Arrange, Act, Assert

    @Test
    fun `format uses JSON config and formats println`() {
        // Arrange
        val tempJson = File.createTempFile("formatter-config", ".json")
        tempJson.writeText(
            """
            {
              "lineBreaksBeforePrints": 2,
              "spaceAroundEquals": true,
              "spaceBeforeColon": false,
              "spaceAfterColon": true
            }
            """.trimIndent(),
        )

        val node = PrintlnNode(LiteralNode("hello", LiteralString))

        // Act
        val result = Formatter.format(node, tempJson)

        // Assert
        assertEquals("\n\nprintln(\"hello\");\n", result)

        // cleanup
        tempJson.delete()
    }
}
