package test.analyzer

import com.google.gson.JsonObject
import main.kotlin.analyzer.AnalyzerVisitorsFactory
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AnalyzerVisitorsFactoryTest {
    @Test
    fun testCreateAnalyzerDefaultVisitorsV10() {
        val factory = AnalyzerVisitorsFactory()
        val visitors = factory.createAnalyzerDefaultVisitors("1.0")
        assertNotNull(visitors)
        assertTrue(visitors.isNotEmpty())
        // V10 debería tener al menos UnusedVariableVisitor y NamingFormatVisitor
        assertTrue(visitors.size >= 2)
    }

    @Test
    fun testCreateAnalyzerDefaultVisitorsV11() {
        val factory = AnalyzerVisitorsFactory()
        val visitors = factory.createAnalyzerDefaultVisitors("1.1")
        assertNotNull(visitors)
        assertTrue(visitors.isNotEmpty())
        // V11 debería tener más visitantes que V10
        assertTrue(visitors.size >= 4)
    }

    @Test
    fun testCreateAnalyzerDefaultVisitorsInvalidVersion() {
        val factory = AnalyzerVisitorsFactory()
        assertThrows(IllegalArgumentException::class.java) {
            factory.createAnalyzerDefaultVisitors("2.0")
        }
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonNull() {
        val factory = AnalyzerVisitorsFactory()
        val visitors = factory.createAnalyzerVisitorsFromJson("1.0", null)
        assertNotNull(visitors)
        assertTrue(visitors.isNotEmpty())
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonV10() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        val unusedVarCheck = JsonObject()
        json.add("UnusedVariableCheck", unusedVarCheck)
        val namingFormatCheck = JsonObject()
        namingFormatCheck.addProperty("namingPatternName", "camelCase")
        json.add("NamingFormatCheck", namingFormatCheck)
        val visitors = factory.createAnalyzerVisitorsFromJson("1.0", json)
        assertNotNull(visitors)
        assertTrue(visitors.isNotEmpty())
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonV11() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        val unusedVarCheck = JsonObject()
        json.add("UnusedVariableCheck", unusedVarCheck)
        val namingFormatCheck = JsonObject()
        namingFormatCheck.addProperty("namingPatternName", "camelCase")
        json.add("NamingFormatCheck", namingFormatCheck)
        val printUseCheck = JsonObject()
        printUseCheck.addProperty("printlnCheckEnabled", true)
        json.add("PrintUseCheck", printUseCheck)
        val readInputCheck = JsonObject()
        readInputCheck.addProperty("readInputCheckEnabled", true)
        json.add("ReadInputCheck", readInputCheck)
        val visitors = factory.createAnalyzerVisitorsFromJson("1.1", json)
        assertNotNull(visitors)
        assertTrue(visitors.isNotEmpty())
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonV10WithPrintUseCheck() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        val printUseCheck = JsonObject()
        printUseCheck.addProperty("printlnCheckEnabled", false)
        json.add("PrintUseCheck", printUseCheck)
        val visitors = factory.createAnalyzerVisitorsFromJson("1.0", json)
        assertNotNull(visitors)
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonWithUnknownCheck() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        val unknownCheck = JsonObject()
        json.add("UnknownCheck", unknownCheck)
        assertThrows(IllegalArgumentException::class.java) {
            factory.createAnalyzerVisitorsFromJson("1.0", json)
        }
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonInvalidVersion() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        assertThrows(IllegalArgumentException::class.java) {
            factory.createAnalyzerVisitorsFromJson("2.0", json)
        }
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonWithSnakeCase() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        val namingFormatCheck = JsonObject()
        namingFormatCheck.addProperty("namingPatternName", "snake_case")
        json.add("NamingFormatCheck", namingFormatCheck)
        val visitors = factory.createAnalyzerVisitorsFromJson("1.0", json)
        assertNotNull(visitors)
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonWithDefaultNamingPattern() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        val namingFormatCheck = JsonObject()
        // No agregar namingPatternName, debería usar el valor por defecto
        json.add("NamingFormatCheck", namingFormatCheck)
        val visitors = factory.createAnalyzerVisitorsFromJson("1.0", json)
        assertNotNull(visitors)
    }

    @Test
    fun testCreateAnalyzerVisitorsFromJsonV11WithAllChecks() {
        val factory = AnalyzerVisitorsFactory()
        val json = JsonObject()
        json.add("UnusedVariableCheck", JsonObject())
        val namingFormatCheck = JsonObject()
        namingFormatCheck.addProperty("namingPatternName", "camelCase")
        json.add("NamingFormatCheck", namingFormatCheck)
        val printUseCheck = JsonObject()
        printUseCheck.addProperty("printlnCheckEnabled", true)
        json.add("PrintUseCheck", printUseCheck)
        val readInputCheck = JsonObject()
        readInputCheck.addProperty("readInputCheckEnabled", false)
        json.add("ReadInputCheck", readInputCheck)
        val visitors = factory.createAnalyzerVisitorsFromJson("1.1", json)
        assertNotNull(visitors)
        assertTrue(visitors.size >= 4)
    }
}
