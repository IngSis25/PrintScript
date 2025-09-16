package formatter

import org.example.LiteralNumber
import org.example.ast.*
import org.example.formatter.FormatterVisitor
import org.example.formatter.config.FormatterConfig
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class FormatterVisitorTest {
    @Test
    fun `if con bloque indentado de 2 espacios y llave en la misma linea`() {
        val config = FormatterConfig(indentSize = 2, ifBraceSameLine = true)
        val out = StringBuilder()
        val v = FormatterVisitor(config, out)

        val ast =
            IfNode(
                condition = IdentifierNode("cond"),
                thenBlock =
                    BlockNode(
                        listOf(
                            VariableDeclarationNode(IdentifierNode("x"), "number", LiteralNode("1", LiteralNumber)),
                        ),
                    ),
                elseBlock = null,
            )
        v.evaluate(ast)

        val expected =
            """
            if (cond) {
              let x: number = 1;
            }
            
            """.trimIndent()
        assertEquals(expected, out.toString())
    }

    @Test
    fun `if-else con statement suelto en then y bloque en else`() {
        val config = FormatterConfig(indentSize = 4, ifBraceSameLine = true)
        val out = StringBuilder()
        val v = FormatterVisitor(config, out)

        val ast =
            IfNode(
                condition = IdentifierNode("ok"),
                thenBlock = PrintlnNode(IdentifierNode("a")),
                elseBlock = BlockNode(listOf(AssignmentNode(IdentifierNode("a"), LiteralNode("2", LiteralNumber)))),
            )
        v.evaluate(ast)

        val expected =
            """
            if (ok) {
                println(a);
            }
            else {
                a = 2;
            }
            
            """.trimIndent()
        assertEquals(expected, out.toString())
    }
}
