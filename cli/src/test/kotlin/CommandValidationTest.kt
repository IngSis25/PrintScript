package main.kotlin.cli

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class CommandValidationTest {
    @TempDir
    lateinit var tempDir: Path

    private lateinit var validFile: File
    private lateinit var invalidFile: File

    @BeforeEach
    fun setUp() {
        validFile = File(tempDir.toFile(), "valid.ps")
        validFile.writeText("println(\"Hello, World!\");")

        invalidFile = File(tempDir.toFile(), "invalid.ps")
        invalidFile.writeText("invalid syntax")
    }

    @Test
    fun `validation command should be created correctly`() {
        val command = ValidationCommand()
        assertNotNull(command)
        assertEquals("validation", command.commandName)
    }

    @Test
    fun `execution command should be created correctly`() {
        val command = ExecutionCommand()
        assertNotNull(command)
        assertEquals("execution", command.commandName)
    }

    @Test
    fun `formatting command should be created correctly`() {
        val command = FormattingCommand()
        assertNotNull(command)
        assertEquals("formatting", command.commandName)
    }

    @Test
    fun `analyzing command should be created correctly`() {
        val command = AnalyzingCommand()
        assertNotNull(command)
        assertEquals("analyzing", command.commandName)
    }

    @Test
    fun `main CLI command should be created correctly`() {
        val command = PrintScriptCLICommand()
        assertNotNull(command)
        assertEquals("printscript", command.commandName)
    }
}
