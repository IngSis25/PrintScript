package runner

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class RunnerTests {
    @Test
    @DisplayName("validate(): reporta error cuando falta ';'")
    fun validate_reports_syntax_error() {
        val code =
            """
            let a: number = 12
            println(a);
            """.trimIndent()

        val runner =
            Runner(
                version = "1.0",
                sourceCode = code,
            )

        val result = runner.validate()
        assertFalse(result.errors.isEmpty(), "Debería reportar error de sintaxis por falta de ';'")
        assertTrue(
            result.errors.first().contains(";") || result.errors.first().contains("syntax", ignoreCase = true),
            "El mensaje debería mencionar un problema de sintaxis o el ';' faltante. Fue: ${result.errors.firstOrNull()}",
        )
    }

    @Test
    @DisplayName("format(): aplica salto de línea tras ';' cuando onlyLineBreakAfterStatement = true")
    fun format_inserts_line_breaks_after_semicolon(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let x:number=1; let y: number=2; println(x + y);
            """.trimIndent()

        // Armamos el runner y validamos que parsee
        val runner =
            Runner(
                version = "1.0",
                sourceCode = code,
            )
        val validate = runner.validate()
        assertTrue(validate.errors.isEmpty(), "El código base debe ser válido antes de formatear")

        // Config mínima del formatter (ajustá si tu schema difiere)
        // Esta bandera la mencionaste en tu suite previa: onlyLineBreakAfterStatement
        val formatterConfig =
            """
            {
              "onlyLineBreakAfterStatement": true
            }
            """.trimIndent()

        val configFile =
            File(tempDir.toFile(), "formatter.json").apply {
                writeText(formatterConfig)
            }

        val formatted = runner.format(configFile)

        // Afirmaciones:
        // 1) No debería haber errores del formatter
        assertTrue(formatted.errors.isEmpty(), "El formateo no debería fallar: ${formatted.errors}")

        // 2) El código debería tener saltos de línea tras ';'
        //    No nos casamos con el estilo exacto, pero al menos esperamos más de una línea.
        val lines = formatted.formattedCode.lines()
        assertTrue(
            lines.size >= 2,
            "Se esperaban múltiples líneas después del formateo. Fue:\n${formatted.formattedCode}",
        )

        // 3) Sugerencia adicional: que siga conteniendo las tres sentencias
        assertTrue(
            formatted.formattedCode.contains("let x") &&
                formatted.formattedCode.contains("let y") &&
                formatted.formattedCode.contains("println"),
            "El formateo no debería eliminar sentencias. Fue:\n${formatted.formattedCode}",
        )
    }
}
