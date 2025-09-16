package main.kotlin.cli

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.IllegalArgumentException

class ResultClassesTest {
    private val testFile = File("test.ps")
    private val testException = IllegalArgumentException("Test error")

    @Test
    fun `ProcessingResult Success should contain correct data`() {
        val tokens = listOf<main.kotlin.lexer.Token>()
        val ast = listOf<org.example.ast.ASTNode>()

        val result = ProcessingResult.Success("source", tokens, ast, testFile, "1.0")

        assertEquals("source", result.sourceCode)
        assertEquals(tokens, result.tokens)
        assertEquals(ast, result.ast)
        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
    }

    @Test
    fun `ProcessingResult Error should contain correct data`() {
        val result = ProcessingResult.Error(testException, testFile, "1.0")

        assertEquals(testException, result.error)
        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
    }

    @Test
    fun `ValidationResult Success should contain correct data`() {
        val result = ValidationResult.Success(testFile, "1.0", 10, 5)

        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
        assertEquals(10, result.tokenCount)
        assertEquals(5, result.statementCount)
    }

    @Test
    fun `ValidationResult Error should contain correct data`() {
        val result = ValidationResult.Error(testException, testFile, "1.0")

        assertEquals(testException, result.error)
        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
    }

    @Test
    fun `ExecutionResult Success should contain correct data`() {
        val result = ExecutionResult.Success(testFile, "1.0", 10, 5)

        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
        assertEquals(10, result.tokenCount)
        assertEquals(5, result.statementCount)
    }

    @Test
    fun `ExecutionResult Error should contain correct data`() {
        val result = ExecutionResult.Error(testException, testFile, "1.0")

        assertEquals(testException, result.error)
        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
    }

    @Test
    fun `FormattingResult Success should contain correct data`() {
        val configFile = File("config.json")
        val outputFile = File("output.ps")

        val result = FormattingResult.Success(testFile, "1.0", 10, 5, "formatted", configFile, outputFile)

        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
        assertEquals(10, result.tokenCount)
        assertEquals(5, result.statementCount)
        assertEquals("formatted", result.formattedCode)
        assertEquals(configFile, result.configFile)
        assertEquals(outputFile, result.outputFile)
    }

    @Test
    fun `FormattingResult Error should contain correct data`() {
        val result = FormattingResult.Error(testException, testFile, "1.0")

        assertEquals(testException, result.error)
        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
    }

    @Test
    fun `AnalysisResult Success should contain correct data`() {
        val configFile = File("config.json")
        val analysisResult = main.kotlin.analyzer.AnalysisResult(emptyList())

        val result = AnalysisResult.Success(testFile, "1.0", 10, 5, analysisResult, configFile)

        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
        assertEquals(10, result.tokenCount)
        assertEquals(5, result.statementCount)
        assertEquals(analysisResult, result.analysisResult)
        assertEquals(configFile, result.configFile)
    }

    @Test
    fun `AnalysisResult Error should contain correct data`() {
        val result = AnalysisResult.Error(testException, testFile, "1.0")

        assertEquals(testException, result.error)
        assertEquals(testFile, result.file)
        assertEquals("1.0", result.version)
    }
}
