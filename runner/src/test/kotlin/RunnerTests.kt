package runner

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.StringReader
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
                reader = StringReader(code),
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

        val runner =
            Runner(
                version = "1.0",
                reader = StringReader(code),
            )
        val validate = runner.validate()
        assertTrue(validate.errors.isEmpty(), "El código base debe ser válido antes de formatear")

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

        val formatted = runner.format(configFile.absolutePath, "1.0")

        assertTrue(formatted.errors.isEmpty(), "El formateo no debería fallar: ${formatted.errors}")

        val lines = formatted.formattedCode.lines()
        assertTrue(
            lines.size >= 2,
            "Se esperaban múltiples líneas después del formateo. Fue:\n${formatted.formattedCode}",
        )

        assertTrue(
            formatted.formattedCode.contains("let x") &&
                formatted.formattedCode.contains("let y") &&
                formatted.formattedCode.contains("println"),
            "El formateo no debería eliminar sentencias. Fue:\n${formatted.formattedCode}",
        )
    }

    @Test
    @DisplayName("analyze(): usa configuración JSON y detecta identificadores inválidos")
    fun analyze_uses_json_configuration() {
        val code =
            """
            let InvalidName: number = 1;
            println(InvalidName);
            """.trimIndent()

        val runner =
            Runner(
                version = "1.0",
                reader = StringReader(code),
            )

        val config =
            buildJsonObject {
                putJsonObject("identifierFormat") {
                    put("enabled", true)
                    put("format", "CAMEL_CASE")
                }
                putJsonObject("printlnRestrictions") {
                    put("enabled", true)
                    put("allowOnlyIdentifiersAndLiterals", true)
                }
                put("maxErrors", 5)
                put("enableWarnings", true)
                put("strictMode", false)
            }

        val result = runner.analyze(config)

        assertTrue(result.errors.isNotEmpty(), "La configuración JSON debería activar el análisis y reportar errores")
        assertTrue(
            result.errors.any { it.contains("Variable name") || it.contains("Identifier") },
            "Se esperaba un error relacionado con el formato del identificador, pero fueron: ${result.errors}",
        )
    }
}
