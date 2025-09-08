package org.example

import main.kotlin.analyzer.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AnalyzerConfigTest {
    @Test
    fun `should create default analyzer config`() {
        val config = AnalyzerConfig()

        assertTrue(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.CAMEL_CASE, config.identifierFormat.format)
        assertTrue(config.printlnRestrictions.enabled)
        assertTrue(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
        assertEquals(100, config.maxErrors)
        assertTrue(config.enableWarnings)
        assertFalse(config.strictMode)
    }

    @Test
    fun `should create custom analyzer config`() {
        val identifierConfig =
            IdentifierFormatConfig(
                enabled = false,
                format = IdentifierFormat.SNAKE_CASE,
            )
        val printlnConfig =
            PrintlnRestrictionConfig(
                enabled = false,
                allowOnlyIdentifiersAndLiterals = false,
            )

        val config =
            AnalyzerConfig(
                identifierFormat = identifierConfig,
                printlnRestrictions = printlnConfig,
                maxErrors = 50,
                enableWarnings = false,
                strictMode = true,
            )

        assertFalse(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.SNAKE_CASE, config.identifierFormat.format)
        assertFalse(config.printlnRestrictions.enabled)
        assertFalse(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
        assertEquals(50, config.maxErrors)
        assertFalse(config.enableWarnings)
        assertTrue(config.strictMode)
    }

    @Test
    fun `should create identifier format config with defaults`() {
        val config = IdentifierFormatConfig()

        assertTrue(config.enabled)
        assertEquals(IdentifierFormat.CAMEL_CASE, config.format)
    }

    @Test
    fun `should create println restriction config with defaults`() {
        val config = PrintlnRestrictionConfig()

        assertTrue(config.enabled)
        assertTrue(config.allowOnlyIdentifiersAndLiterals)
    }

    @Test
    fun `should support all identifier formats`() {
        val camelCase = IdentifierFormatConfig(format = IdentifierFormat.CAMEL_CASE)
        val snakeCase = IdentifierFormatConfig(format = IdentifierFormat.SNAKE_CASE)
        val pascalCase = IdentifierFormatConfig(format = IdentifierFormat.PASCAL_CASE)

        assertEquals(IdentifierFormat.CAMEL_CASE, camelCase.format)
        assertEquals(IdentifierFormat.SNAKE_CASE, snakeCase.format)
        assertEquals(IdentifierFormat.PASCAL_CASE, pascalCase.format)
    }
}
