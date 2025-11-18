package test.lexerTest

import main.kotlin.lexer.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun testPositionCreation() {
        val position = Position(1, 1)
        assertEquals(1, position.line)
        assertEquals(1, position.column)
    }

    @Test
    fun testPositionModification() {
        val position = Position(1, 1)
        position.line = 5
        position.column = 10
        assertEquals(5, position.line)
        assertEquals(10, position.column)
    }

    @Test
    fun testPositionMultipleModifications() {
        val position = Position(1, 1)
        position.line++
        position.column += 5
        assertEquals(2, position.line)
        assertEquals(6, position.column)
    }

    @Test
    fun testPositionReset() {
        val position = Position(5, 10)
        position.line = 1
        position.column = 1
        assertEquals(1, position.line)
        assertEquals(1, position.column)
    }
}
