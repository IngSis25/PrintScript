package cli

import com.github.ajalt.clikt.testing.test
import main.kotlin.cli.Validate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class ValidateTest {
    @Test
    @DisplayName("Validate: código válido debe pasar la validación")
    fun validate_validCode_shouldSucceed(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let a: number = 10;
            println(a);
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val command = Validate()
        val result = command.test(testFile.absolutePath)

        assertTrue(result.output.contains("Validation completed"))
        assertEquals(0, result.statusCode)
    }

    @Test
    @DisplayName("Validate: código inválido debe fallar")
    fun validate_invalidCode_shouldFail(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let a: number = 10
            println(a);
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val command = Validate()

        assertThrows(Exception::class.java) {
            command.test(testFile.absolutePath)
        }
    }

    @Test
    @DisplayName("Validate: código con múltiples declaraciones válidas")
    fun validate_multipleValidStatements_shouldSucceed(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let x: number = 5;
            let y: number = 10;
            let z: number = x + y;
            println(z);
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val command = Validate()
        val result = command.test(testFile.absolutePath)

        assertTrue(result.output.contains("Validation completed"))
        assertEquals(0, result.statusCode)
    }
}
