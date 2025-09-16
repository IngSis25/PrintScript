package main.kotlin.cli

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class PrintScriptCLIConfigTest {
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
    fun `format should create default formatter config when none exists`() {
        val result = cli.format(testFile, null, null, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertTrue(success.configFile.exists())
        assertTrue(success.configFile.name.endsWith(".json"))

        // Verify config file content
        val configContent = success.configFile.readText()
        assertTrue(configContent.contains("lineBreaksBeforePrints"))
        assertTrue(configContent.contains("spaceAroundEquals"))
    }

    @Test
    fun `analyze should create default analyzer config when none exists`() {
        val result = cli.analyze(testFile, null, "1.0") { }

        assertTrue(result is AnalysisResult.Success)
        val success = result as AnalysisResult.Success
        assertTrue(success.configFile.exists())
        assertTrue(success.configFile.name.endsWith(".json"))

        // Verify config file content
        val configContent = success.configFile.readText()
        assertTrue(configContent.contains("identifierFormat"))
        assertTrue(configContent.contains("printlnRestrictions"))
    }

    @Test
    fun `format should use existing config file when provided`() {
        val configFile = File(tempDir.toFile(), "custom-formatter.json")
        configFile.writeText(
            """
            {
              "lineBreaksBeforePrints": 2,
              "spaceAroundEquals": false,
              "spaceBeforeColon": true,
              "spaceAfterColon": true,
              "spaceAroundAssignment": false
            }
            """.trimIndent(),
        )

        val result = cli.format(testFile, configFile, null, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertEquals(configFile, success.configFile)
    }

    @Test
    fun `analyze should use existing config file when provided`() {
        val configFile = File(tempDir.toFile(), "custom-analyzer.json")
        configFile.writeText(
            """
            {
              "identifierFormat": {
                "enabled": false,
                "format": "SNAKE_CASE"
              },
              "printlnRestrictions": {
                "enabled": false,
                "allowOnlyIdentifiersAndLiterals": false
              },
              "maxErrors": 5,
              "enableWarnings": false,
              "strictMode": true
            }
            """.trimIndent(),
        )

        val result = cli.analyze(testFile, configFile, "1.0") { }

        assertTrue(result is AnalysisResult.Success)
        val success = result as AnalysisResult.Success
        assertEquals(configFile, success.configFile)
    }

    @Test
    fun `format should write to output file when specified`() {
        val outputFile = File(tempDir.toFile(), "formatted-output.ps")

        val result = cli.format(testFile, null, outputFile, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertEquals(outputFile, success.outputFile)
        assertTrue(outputFile.exists())
        assertTrue(outputFile.readText().isNotEmpty())
    }

    @Test
    fun `format should not create output file when not specified`() {
        val result = cli.format(testFile, null, null, "1.0") { }

        assertTrue(result is FormattingResult.Success)
        val success = result as FormattingResult.Success
        assertNull(success.outputFile)
    }
}
