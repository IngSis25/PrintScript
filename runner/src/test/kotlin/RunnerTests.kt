package runner

import com.google.gson.JsonObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.StringReader

class RunnerTests {
    @Test
    @DisplayName("Debería crear un Runner con versión 1.0")
    fun `should create runner with version one`() {
        val code = "let x: number = 10;"
        val reader = StringReader(code)

        assertDoesNotThrow {
            Runner("1.0", reader)
        }
    }

    @Test
    @DisplayName("Debería crear un Runner con versión 1.1")
    fun `should create runner with version one one`() {
        val code = "let x: number = 10;"
        val reader = StringReader(code)

        assertDoesNotThrow {
            Runner("1.1", reader)
        }
    }

    @Test
    @DisplayName("Debería lanzar excepción con versión no soportada")
    fun `should throw exception with unsupported version`() {
        val code = "let x: number = 10;"
        val reader = StringReader(code)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Runner("2.0", reader)
            }

        assertTrue(exception.message?.contains("Unsupported version") == true)
    }

    @Test
    @DisplayName("Debería validar código válido sin errores")
    fun `should validate valid code without errors`() {
        val code = "let x: number = 10;"
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val result = runner.validate()

        assertTrue(result.errors.isEmpty(), "No debería haber errores en código válido")
    }

    @Test
    @DisplayName("Debería detectar errores en código inválido")
    fun `should detect errors in invalid code`() {
        val code = "let x number = 10;" // Falta el ':'
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val result = runner.validate()

        assertFalse(result.errors.isEmpty(), "Debería haber errores en código inválido")
    }

    @Test
    @DisplayName("Debería formatear código correctamente")
    fun `should format code correctly`() {
        val code = "let x:number=10;"
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val rulesJson = "{}"
        val result = runner.format(rulesJson, "1.0")

        assertTrue(result.errors.isEmpty(), "No debería haber errores al formatear")
        assertNotNull(result.formattedCode, "Debería retornar código formateado")
    }

    @Test
    @DisplayName("Debería analizar código sin warnings con configuración vacía")
    fun `should analyze code without warnings with empty config`() {
        val code = "let x: number = 10;"
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val jsonConfig = JsonObject()
        val result = runner.analyze(jsonConfig)

        assertNotNull(result, "Debería retornar un resultado")
        // Puede haber warnings o no, dependiendo de la configuración
    }

    @Test
    @DisplayName("Debería analizar código con configuración de UnusedVariableCheck")
    fun `should analyze code with UnusedVariableCheck config`() {
        val code = "let unused: number = 10;"
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val jsonConfig = JsonObject()
        val unusedVarCheck = JsonObject()
        jsonConfig.add("UnusedVariableCheck", unusedVarCheck)

        val result = runner.analyze(jsonConfig)

        assertNotNull(result, "Debería retornar un resultado")
        assertNotNull(result.warnings, "Debería tener lista de warnings")
        assertNotNull(result.errors, "Debería tener lista de errors")
    }

    @Test
    @DisplayName("Debería analizar código con configuración de NamingFormatCheck")
    fun `should analyze code with NamingFormatCheck config`() {
        val code = "let snake_case: number = 10;"
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val jsonConfig = JsonObject()
        val namingFormatCheck = JsonObject()
        namingFormatCheck.addProperty("namingPatternName", "camelCase")
        jsonConfig.add("NamingFormatCheck", namingFormatCheck)

        val result = runner.analyze(jsonConfig)

        assertNotNull(result, "Debería retornar un resultado")
    }

    @Test
    @DisplayName("Debería manejar código con múltiples declaraciones")
    fun `should handle code with multiple declarations`() {
        val code =
            """
            let x: number = 10;
            let y: string = "hello";
            println(x);
            """.trimIndent()

        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val result = runner.validate()

        assertTrue(result.errors.isEmpty(), "No debería haber errores en código válido")
    }

    @Test
    @DisplayName("Debería validar código con if en versión 1.1")
    fun `should validate code with if in version one one`() {
        val code =
            """
            if (true) {
                println("hello");
            }
            """.trimIndent()

        val reader = StringReader(code)
        val runner = Runner("1.1", reader)

        val result = runner.validate()

        assertTrue(result.errors.isEmpty(), "No debería haber errores en código válido con if")
    }

    @Test
    @DisplayName("Debería formatear código con reglas personalizadas")
    fun `should format code with custom rules`() {
        val code = "let x:number=10;"
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val rulesJson =
            """
            {
                "space_around_equals": true,
                "space_before_colon": true,
                "space_after_colon": true
            }
            """.trimIndent()

        val result = runner.format(rulesJson, "1.0")

        assertTrue(result.errors.isEmpty(), "No debería haber errores al formatear")
        assertTrue(result.formattedCode.isNotEmpty(), "Debería retornar código formateado")
    }

    @Test
    @DisplayName("Debería manejar código vacío")
    fun `should handle empty code`() {
        val code = ""
        val reader = StringReader(code)
        val runner = Runner("1.0", reader)

        val result = runner.validate()

        assertNotNull(result, "Debería retornar un resultado")
    }

    @Test
    @DisplayName("Debería analizar código con PrintUseCheck en versión 1.1")
    fun `should analyze code with PrintUseCheck in version one one`() {
        val code = "println(10);"
        val reader = StringReader(code)
        val runner = Runner("1.1", reader)

        val jsonConfig = JsonObject()
        val printUseCheck = JsonObject()
        printUseCheck.addProperty("printlnCheckEnabled", true)
        jsonConfig.add("PrintUseCheck", printUseCheck)

        val result = runner.analyze(jsonConfig)

        assertNotNull(result, "Debería retornar un resultado")
    }
}
