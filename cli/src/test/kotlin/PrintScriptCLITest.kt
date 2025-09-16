package main.kotlin.cli

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class PrintScriptCLITest {
    @TempDir
    lateinit var tempDir: Path

    private lateinit var cli: PrintScriptCLI
    private lateinit var testFile: File

    @BeforeEach
    fun setUp() {
        cli = PrintScriptCLI()
        testFile = File(tempDir.toFile(), "test.ps")
        testFile.writeText("println(\"Hello, World!\");")
    }

    @Test
    fun `processFile should return Success for valid PrintScript file`() {
        val result = cli.processFile(testFile, "1.0") { }

        assertTrue(result is ProcessingResult.Success)
        val success = result as ProcessingResult.Success
        assertEquals(testFile, success.file)
        assertEquals("1.0", success.version)
        assertTrue(success.tokens.isNotEmpty())
        assertTrue(success.ast.isNotEmpty())
    }

    @Test
    fun `processFile should return Error for invalid file`() {
        val invalidFile = File(tempDir.toFile(), "invalid.ps")
        invalidFile.writeText("invalid syntax")

        val result = cli.processFile(invalidFile, "1.0") { }

        assertTrue(result is ProcessingResult.Error)
        val error = result as ProcessingResult.Error
        assertEquals(invalidFile, error.file)
        assertEquals("1.0", error.version)
        assertNotNull(error.error)
    }

    @Test
    fun `validate should return Success for valid file`() {
        val result = cli.validate(testFile, "1.0") { }

        assertTrue(result is ValidationResult.Success)
        val success = result as ValidationResult.Success
        assertEquals(testFile, success.file)
        assertEquals("1.0", success.version)
        assertTrue(success.tokenCount > 0)
        assertTrue(success.statementCount > 0)
    }

    @Test
    fun `validate should return Error for invalid file`() {
        val invalidFile = File(tempDir.toFile(), "invalid.ps")
        invalidFile.writeText("invalid syntax")

        val result = cli.validate(invalidFile, "1.0") { }

        assertTrue(result is ValidationResult.Error)
        val error = result as ValidationResult.Error
        assertEquals(invalidFile, error.file)
        assertNotNull(error.error)
    }

    @Test
    fun `execute should return Success for valid file`() {
        val result = cli.execute(testFile, "1.0") { }

        assertTrue(result is ExecutionResult.Success)
        val success = result as ExecutionResult.Success
        assertEquals(testFile, success.file)
        assertEquals("1.0", success.version)
        assertTrue(success.tokenCount > 0)
        assertTrue(success.statementCount > 0)
    }

    @Test
    fun `execute should return Error for invalid file`() {
        val invalidFile = File(tempDir.toFile(), "invalid.ps")
        invalidFile.writeText("invalid syntax")

        val result = cli.execute(invalidFile, "1.0") { }

        assertTrue(result is ExecutionResult.Error)
        val error = result as ExecutionResult.Error
        assertEquals(invalidFile, error.file)
        assertNotNull(error.error)
    }

    @Test
    fun `format should return Success for valid file`() {
        val result = cli.format(testFile, null, null, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertEquals(testFile, success.file)
        assertEquals("1.0", success.version)
        assertTrue(success.formattedCode.isNotEmpty())
        assertTrue(success.tokenCount > 0)
        assertTrue(success.statementCount > 0)
    }

    @Test
    fun `format should return Error for invalid file`() {
        val invalidFile = File(tempDir.toFile(), "invalid.ps")
        invalidFile.writeText("invalid syntax")

        val result = cli.format(invalidFile, null, null, "1.0") { }

        assertTrue(result is FormattingResult.Error)
        val error = result as FormattingResult.Error
        assertEquals(invalidFile, error.file)
        assertNotNull(error.error)
    }

    @Test
    fun `format should create default config file when none exists`() {
        val result = cli.format(testFile, null, null, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertTrue(success.configFile.exists())
    }

    @Test
    fun `format should write to output file when specified`() {
        val outputFile = File(tempDir.toFile(), "output.ps")
        val result = cli.format(testFile, null, outputFile, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertEquals(outputFile, success.outputFile)
        assertTrue(outputFile.exists())
    }

    @Test
    fun `analyze should return Success for valid file`() {
        val result = cli.analyze(testFile, null, "1.0") { }

        assertTrue(result is AnalysisResult.Success)
        val success = result as AnalysisResult.Success
        assertEquals(testFile, success.file)
        assertEquals("1.0", success.version)
        assertTrue(success.tokenCount > 0)
        assertTrue(success.statementCount > 0)
        assertNotNull(success.analysisResult)
    }

    @Test
    fun `analyze should return Error for invalid file`() {
        val invalidFile = File(tempDir.toFile(), "invalid.ps")
        invalidFile.writeText("invalid syntax")

        val result = cli.analyze(invalidFile, null, "1.0") { }

        assertTrue(result is AnalysisResult.Error)
        val error = result as AnalysisResult.Error
        assertEquals(invalidFile, error.file)
        assertNotNull(error.error)
    }

    @Test
    fun `analyze should create default config file when none exists`() {
        val result = cli.analyze(testFile, null, "1.0") { }

        assertTrue(result is AnalysisResult.Success)
        val success = result as AnalysisResult.Success
        assertTrue(success.configFile.exists())
    }

    @Test
    fun `processFile should call progress callback`() {
        val progressMessages = mutableListOf<String>()

        cli.processFile(testFile, "1.0") { message ->
            progressMessages.add(message)
        }

        assertTrue(progressMessages.isNotEmpty())
        assertTrue(progressMessages.any { it.contains("Reading source file") })
        assertTrue(progressMessages.any { it.contains("Performing lexical analysis") })
        assertTrue(progressMessages.any { it.contains("Performing syntax analysis") })
    }
}
