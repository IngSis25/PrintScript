package test.analyzer

import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.IdentifierFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AnalyzerConfigTest {
    @Test
    fun testDefaultConfig() {
        val config = AnalyzerConfig()
        assertEquals(100, config.maxErrors)
        assertTrue(config.enableWarnings)
        assertFalse(config.strictMode)
        assertTrue(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.CAMEL_CASE, config.identifierFormat.format)
        assertTrue(config.printlnRestrictions.enabled)
        assertTrue(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
    }

    @Test
    fun testConfigWithCustomMaxErrors() {
        val config = AnalyzerConfig(maxErrors = 50)
        assertEquals(50, config.maxErrors)
    }

    @Test
    fun testConfigWithDisabledWarnings() {
        val config = AnalyzerConfig(enableWarnings = false)
        assertFalse(config.enableWarnings)
    }

    @Test
    fun testConfigWithStrictMode() {
        val config = AnalyzerConfig(strictMode = true)
        assertTrue(config.strictMode)
    }

    @Test
    fun testConfigWithIdentifierFormat() {
        val identifierFormat =
            main.kotlin.analyzer.IdentifierFormatConfig(
                enabled = false,
                format = IdentifierFormat.SNAKE_CASE,
            )
        val config = AnalyzerConfig(identifierFormat = identifierFormat)
        assertFalse(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.SNAKE_CASE, config.identifierFormat.format)
    }

    @Test
    fun testConfigWithPrintlnRestrictions() {
        val printlnRestrictions =
            main.kotlin.analyzer.PrintlnRestrictionConfig(
                enabled = false,
                allowOnlyIdentifiersAndLiterals = false,
            )
        val config = AnalyzerConfig(printlnRestrictions = printlnRestrictions)
        assertFalse(config.printlnRestrictions.enabled)
        assertFalse(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
    }

    @Test
    fun testIdentifierFormatConfigDefault() {
        val config = main.kotlin.analyzer.IdentifierFormatConfig()
        assertTrue(config.enabled)
        assertEquals(IdentifierFormat.CAMEL_CASE, config.format)
    }

    @Test
    fun testPrintlnRestrictionConfigDefault() {
        val config = main.kotlin.analyzer.PrintlnRestrictionConfig()
        assertTrue(config.enabled)
        assertTrue(config.allowOnlyIdentifiersAndLiterals)
    }

    @Test
    fun testAllIdentifierFormats() {
        assertEquals(IdentifierFormat.CAMEL_CASE, IdentifierFormat.CAMEL_CASE)
        assertEquals(IdentifierFormat.SNAKE_CASE, IdentifierFormat.SNAKE_CASE)
        assertEquals(IdentifierFormat.PASCAL_CASE, IdentifierFormat.PASCAL_CASE)
    }
}
