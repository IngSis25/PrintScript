package org.example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EnvironmentTest {
    @Test
    fun testGetGlobalVariableGravity() {
        val value = Environment.getGlobalVariable("gravity")
        assertTrue(value is Double)
        assertEquals(9.81, value as Double, 0.001)
    }

    @Test
    fun testGetGlobalVariablePi() {
        val value = Environment.getGlobalVariable("pi")
        assertTrue(value is Double)
        assertEquals(3.1415, value as Double, 0.001)
    }

    @Test
    fun testGetGlobalVariableBestFootballClub() {
        val value = Environment.getGlobalVariable("BEST_FOOTBALL_CLUB")
        assertTrue(value is String)
        assertEquals("San Lorenzo", value as String)
    }

    @Test
    fun testGetGlobalVariableNonExistent() {
        val value = Environment.getGlobalVariable("NON_EXISTENT_VARIABLE")
        assertNull(value)
    }

    @Test
    fun testGetGlobalVariableEmptyString() {
        val value = Environment.getGlobalVariable("")
        assertNull(value)
    }

    @Test
    fun testGetGlobalVariableCaseSensitive() {
        // Verificar que es case-sensitive
        val value = Environment.getGlobalVariable("Gravity")
        assertNull(value) // Debería ser null porque "gravity" es minúscula
    }

    @Test
    fun testGetGlobalVariableWithSpaces() {
        val value = Environment.getGlobalVariable("gravity ")
        assertNull(value) // Debería ser null porque tiene espacio
    }

    @Test
    fun testHasGlobalVariableGravity() {
        assertTrue(Environment.hasGlobalVariable("gravity"))
    }

    @Test
    fun testHasGlobalVariablePi() {
        assertTrue(Environment.hasGlobalVariable("pi"))
    }

    @Test
    fun testHasGlobalVariableBestFootballClub() {
        assertTrue(Environment.hasGlobalVariable("BEST_FOOTBALL_CLUB"))
    }

    @Test
    fun testHasGlobalVariableNonExistent() {
        assertFalse(Environment.hasGlobalVariable("NON_EXISTENT_VARIABLE"))
    }

    @Test
    fun testHasGlobalVariableEmptyString() {
        assertFalse(Environment.hasGlobalVariable(""))
    }

    @Test
    fun testHasGlobalVariableCaseSensitive() {
        // Verificar que es case-sensitive
        assertFalse(Environment.hasGlobalVariable("Gravity"))
    }

    @Test
    fun testHasGlobalVariableWithSpaces() {
        assertFalse(Environment.hasGlobalVariable("gravity "))
    }

    @Test
    fun testGetGlobalVariableReturnsCorrectType() {
        val gravity = Environment.getGlobalVariable("gravity")
        assertTrue(gravity is Number)

        val pi = Environment.getGlobalVariable("pi")
        assertTrue(pi is Number)

        val club = Environment.getGlobalVariable("BEST_FOOTBALL_CLUB")
        assertTrue(club is String)
    }

    @Test
    fun testHasGlobalVariableConsistencyWithGet() {
        // Si hasGlobalVariable devuelve true, getGlobalVariable no debería ser null
        if (Environment.hasGlobalVariable("gravity")) {
            assertTrue(Environment.getGlobalVariable("gravity") != null)
        }

        if (Environment.hasGlobalVariable("pi")) {
            assertTrue(Environment.getGlobalVariable("pi") != null)
        }

        if (Environment.hasGlobalVariable("BEST_FOOTBALL_CLUB")) {
            assertTrue(Environment.getGlobalVariable("BEST_FOOTBALL_CLUB") != null)
        }

        // Si hasGlobalVariable devuelve false, getGlobalVariable debería ser null
        if (!Environment.hasGlobalVariable("NON_EXISTENT")) {
            assertNull(Environment.getGlobalVariable("NON_EXISTENT"))
        }
    }

    @Test
    fun testGetGlobalVariableAllVariables() {
        // Verificar que todas las variables definidas existen y tienen valores correctos
        val gravity = Environment.getGlobalVariable("gravity")
        assertTrue(gravity != null)
        assertEquals(9.81, gravity as Double, 0.001)

        val pi = Environment.getGlobalVariable("pi")
        assertTrue(pi != null)
        assertEquals(3.1415, pi as Double, 0.001)

        val club = Environment.getGlobalVariable("BEST_FOOTBALL_CLUB")
        assertTrue(club != null)
        assertEquals("San Lorenzo", club as String)
    }

    @Test
    fun testHasGlobalVariableAllVariables() {
        // Verificar que todas las variables definidas existen
        assertTrue(Environment.hasGlobalVariable("gravity"))
        assertTrue(Environment.hasGlobalVariable("pi"))
        assertTrue(Environment.hasGlobalVariable("BEST_FOOTBALL_CLUB"))
    }

    @Test
    fun testGetGlobalVariableWithSpecialCharacters() {
        val value = Environment.getGlobalVariable("gravity@#$")
        assertNull(value)
    }

    @Test
    fun testHasGlobalVariableWithSpecialCharacters() {
        assertFalse(Environment.hasGlobalVariable("gravity@#$"))
    }

    @Test
    fun testGetGlobalVariableWithNumbers() {
        val value = Environment.getGlobalVariable("gravity123")
        assertNull(value)
    }

    @Test
    fun testHasGlobalVariableWithNumbers() {
        assertFalse(Environment.hasGlobalVariable("gravity123"))
    }

    @Test
    fun testGetGlobalVariableWithVeryLongName() {
        val longName = "a".repeat(1000)
        val value = Environment.getGlobalVariable(longName)
        assertNull(value)
    }

    @Test
    fun testHasGlobalVariableWithVeryLongName() {
        val longName = "a".repeat(1000)
        assertFalse(Environment.hasGlobalVariable(longName))
    }
}
