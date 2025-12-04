package test.analyzer

import main.kotlin.analyzer.ConfigLoader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class ConfigLoaderTest {
    @Test
    fun testLoadFromJsonStringValid() {
        val json =
            """
            {
                "maxErrors": 50,
                "enableWarnings": true,
                "strictMode": false
            }
            """.trimIndent()
        val config = ConfigLoader.loadFromJsonString(json)
        assertNotNull(config)
        assertEquals(50, config.maxErrors)
        assertTrue(config.enableWarnings)
        assertFalse(config.strictMode)
    }

    @Test
    fun testLoadFromJsonStringInvalid() {
        val json = "invalid json"
        assertThrows(IllegalArgumentException::class.java) {
            ConfigLoader.loadFromJsonString(json)
        }
    }

    @Test
    fun testLoadFromJsonStringEmpty() {
        val json = "{}"
        val config = ConfigLoader.loadFromJsonString(json)
        assertNotNull(config)
        // Debería usar valores por defecto
        assertEquals(100, config.maxErrors)
    }

    @Test
    fun testLoadFromJsonFile() {
        val tempFile = File.createTempFile("test-config", ".json")
        try {
            tempFile.writeText(
                """
                {
                    "maxErrors": 25,
                    "enableWarnings": false
                }
                """.trimIndent(),
            )
            val config = ConfigLoader.loadFromJson(tempFile)
            assertNotNull(config)
            assertEquals(25, config.maxErrors)
            assertFalse(config.enableWarnings)
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testLoadFromJsonFileNotExists() {
        val nonExistentFile = File("non-existent-file.json")
        assertThrows(IllegalArgumentException::class.java) {
            ConfigLoader.loadFromJson(nonExistentFile)
        }
    }

    @Test
    fun testLoadFromJsonFileInvalidJson() {
        val tempFile = File.createTempFile("test-config-invalid", ".json")
        try {
            tempFile.writeText("invalid json content")
            assertThrows(IllegalArgumentException::class.java) {
                ConfigLoader.loadFromJson(tempFile)
            }
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testLoadWithFallbackWithValidPath() {
        val tempFile = File.createTempFile("test-config", ".json")
        try {
            tempFile.writeText(
                """
                {
                    "maxErrors": 75
                }
                """.trimIndent(),
            )
            val config = ConfigLoader.loadWithFallback(tempFile.absolutePath)
            assertNotNull(config)
            assertEquals(75, config.maxErrors)
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testLoadWithFallbackWithNull() {
        val config = ConfigLoader.loadWithFallback(null)
        assertNotNull(config)
        // Debería usar configuración por defecto
        assertEquals(100, config.maxErrors)
    }

    @Test
    fun testLoadWithFallbackWithInvalidPath() {
        val config = ConfigLoader.loadWithFallback("non-existent-file.json")
        assertNotNull(config)
        // Debería usar configuración por defecto en caso de error
        assertEquals(100, config.maxErrors)
    }

    @Test
    fun testCreateDefaultConfig() {
        val tempFile = File.createTempFile("default-config", ".json")
        try {
            ConfigLoader.createDefaultConfig(tempFile)
            assertTrue(tempFile.exists())
            assertTrue(tempFile.canRead())
            val content = tempFile.readText()
            assertTrue(content.contains("maxErrors"))
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testIsValidConfigFileValid() {
        val tempFile = File.createTempFile("valid-config", ".json")
        try {
            tempFile.writeText(
                """
                {
                    "maxErrors": 50
                }
                """.trimIndent(),
            )
            assertTrue(ConfigLoader.isValidConfigFile(tempFile))
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testIsValidConfigFileInvalid() {
        val tempFile = File.createTempFile("invalid-config", ".json")
        try {
            tempFile.writeText("invalid json")
            assertFalse(ConfigLoader.isValidConfigFile(tempFile))
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testIsValidConfigFileNotExists() {
        val nonExistentFile = File("non-existent-config.json")
        assertFalse(ConfigLoader.isValidConfigFile(nonExistentFile))
    }

    @Test
    fun testLoadFromJsonFileUnreadable() {
        val tempFile = File.createTempFile("unreadable-config", ".json")
        try {
            tempFile.writeText(
                """
                {
                    "maxErrors": 50
                }
                """.trimIndent(),
            )
            // En algunos sistemas no podemos hacer un archivo realmente no legible,
            // pero podemos probar el caso donde no existe
            tempFile.setReadable(false)
            if (!tempFile.canRead()) {
                assertThrows(IllegalArgumentException::class.java) {
                    ConfigLoader.loadFromJson(tempFile)
                }
            }
        } finally {
            tempFile.setReadable(true)
            tempFile.delete()
        }
    }

    @Test
    fun testLoadFromJsonStringWithIdentifierFormat() {
        val json =
            """
            {
                "identifierFormat": {
                    "enabled": true,
                    "format": "CAMEL_CASE"
                }
            }
            """.trimIndent()
        val config = ConfigLoader.loadFromJsonString(json)
        assertNotNull(config)
        assertTrue(config.identifierFormat.enabled)
    }

    @Test
    fun testLoadFromJsonStringWithPrintlnRestrictions() {
        val json =
            """
            {
                "printlnRestrictions": {
                    "enabled": true,
                    "allowOnlyIdentifiersAndLiterals": false
                }
            }
            """.trimIndent()
        val config = ConfigLoader.loadFromJsonString(json)
        assertNotNull(config)
        assertTrue(config.printlnRestrictions.enabled)
        assertFalse(config.printlnRestrictions.allowOnlyIdentifiersAndLiterals)
    }
}
