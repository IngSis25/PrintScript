package cli

import com.github.ajalt.clikt.testing.test
import main.kotlin.cli.Execute
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class ExecuteTest {
    @Test
    @DisplayName("Execute: código válido debe ejecutarse correctamente")
    fun execute_validCode_shouldSucceed(
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

        val command = Execute()
        val result = command.test(testFile.absolutePath)

        assertTrue(result.output.contains("Execution successful"))
        assertEquals(0, result.statusCode)
    }

    @Test
    @DisplayName("Execute: archivo inexistente debe reportar error")
    fun execute_nonexistentFile_shouldReportError() {
        val command = Execute()
        val result = command.test("nonexistent.ps")

        assertTrue(result.output.contains("Error") || result.output.contains("File not found"))
    }

    @Test
    @DisplayName("Execute: código con operaciones matemáticas")
    fun execute_arithmeticOperations_shouldSucceed(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let x: number = 5;
            let y: number = 3;
            let result: number = x + y;
            println(result);
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val command = Execute()
        val result = command.test(testFile.absolutePath)

        assertTrue(result.output.contains("Execution successful"))
    }

    @Test
    @DisplayName("Execute: código con error de sintaxis debe fallar")
    fun execute_syntaxError_shouldFail(
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

        val command = Execute()

        assertThrows(Exception::class.java) {
            command.test(testFile.absolutePath)
        }
    }
}
