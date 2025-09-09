package org.example

import main.kotlin.analyzer.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class ConfigLoaderTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `should load configuration from JSON string`() {
        val jsonConfig =
            """
            {
              "identifierFormat": {
                "enabled": true,
                "format": "SNAKE_CASE"
              },
              "printlnRestrictions": {
                "enabled": false,
                "allowOnlyIdentifiersAndLiterals": false
              },
              "maxErrors": 50,
              "enableWarnings": false,
              "strictMode": true
            }
            """.trimIndent()

        val config = ConfigLoader.loadFromJsonString(jsonConfig)

        assertTrue(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.SNAKE_CASE, config.identifierFormat.format)
        assertFalse(config.printlnRestrictions.enabled)
        assertFalse(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
        assertEquals(50, config.maxErrors)
        assertFalse(config.enableWarnings)
        assertTrue(config.strictMode)
    }

    @Test
    fun `should load configuration from JSON file`() {
        val jsonConfig =
            """
            {
              "identifierFormat": {
                "enabled": true,
                "format": "PASCAL_CASE"
              },
              "printlnRestrictions": {
                "enabled": true,
                "allowOnlyIdentifiersAndLiterals": true
              },
              "maxErrors": 200,
              "enableWarnings": true,
              "strictMode": false
            }
            """.trimIndent()

        val configFile = File(tempDir.toFile(), "test-config.json")
        configFile.writeText(jsonConfig)

        val config = ConfigLoader.loadFromJson(configFile)

        assertTrue(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.PASCAL_CASE, config.identifierFormat.format)
        assertTrue(config.printlnRestrictions.enabled)
        assertTrue(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
        assertEquals(200, config.maxErrors)
        assertTrue(config.enableWarnings)
        assertFalse(config.strictMode)
    }

    @Test
    fun `should throw exception for non-existent file`() {
        val nonExistentFile = File(tempDir.toFile(), "non-existent.json")

        assertThrows(IllegalArgumentException::class.java) {
            ConfigLoader.loadFromJson(nonExistentFile)
        }
    }

    @Test
    fun `should throw exception for invalid JSON`() {
        val invalidJson = "{ invalid json }"

        assertThrows(IllegalArgumentException::class.java) {
            ConfigLoader.loadFromJsonString(invalidJson)
        }
    }

    @Test
    fun `should load with fallback when file does not exist`() {
        val nonExistentPath = "non-existent.json"

        val config = ConfigLoader.loadWithFallback(nonExistentPath)

        // Should return default configuration
        assertTrue(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.CAMEL_CASE, config.identifierFormat.format)
        assertEquals(100, config.maxErrors)
    }

    @Test
    fun `should load with fallback when path is null`() {
        val config = ConfigLoader.loadWithFallback(null)

        // Should return default configuration
        assertTrue(config.identifierFormat.enabled)
        assertEquals(IdentifierFormat.CAMEL_CASE, config.identifierFormat.format)
        assertEquals(100, config.maxErrors)
    }

    @Test
    fun `should create default configuration file`() {
        val configFile = File(tempDir.toFile(), "default-config.json")

        ConfigLoader.createDefaultConfig(configFile)

        assertTrue(configFile.exists())
        assertTrue(configFile.canRead())

        // Verify the file contains valid JSON
        val loadedConfig = ConfigLoader.loadFromJson(configFile)
        assertNotNull(loadedConfig)
    }

    @Test
    fun `should validate configuration file correctly`() {
        val validConfigFile = File(tempDir.toFile(), "valid-config.json")
        val invalidConfigFile = File(tempDir.toFile(), "invalid-config.json")
        val nonExistentFile = File(tempDir.toFile(), "non-existent.json")

        // Create valid config file
        val validJson =
            """
            {
              "identifierFormat": {
                "enabled": true,
                "format": "CAMEL_CASE"
              },
              "printlnRestrictions": {
                "enabled": true,
                "allowOnlyIdentifiersAndLiterals": true
              },
              "maxErrors": 100,
              "enableWarnings": true,
              "strictMode": false
            }
            """.trimIndent()
        validConfigFile.writeText(validJson)

        // Create invalid config file
        invalidConfigFile.writeText("{ invalid json }")

        assertTrue(ConfigLoader.isValidConfigFile(validConfigFile))
        assertFalse(ConfigLoader.isValidConfigFile(invalidConfigFile))
        assertFalse(ConfigLoader.isValidConfigFile(nonExistentFile))
    }
}
