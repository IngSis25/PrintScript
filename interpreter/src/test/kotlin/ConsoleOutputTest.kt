package org.example.output

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleOutputTest {
    private val originalOut = System.out
    private val outputStream = ByteArrayOutputStream()
    private val printStream = PrintStream(outputStream)

    @BeforeEach
    fun setUp() {
        System.setOut(printStream)
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    @Test
    fun testWriteSimpleString() {
        val consoleOutput = ConsoleOutput()
        val message = "Hello, World!"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteEmptyString() {
        val consoleOutput = ConsoleOutput()
        val message = ""

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteWithNewline() {
        val consoleOutput = ConsoleOutput()
        val message = "Line 1\nLine 2"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteMultipleTimes() {
        val consoleOutput = ConsoleOutput()
        val message1 = "First message"
        val message2 = "Second message"

        consoleOutput.write(message1)
        consoleOutput.write(message2)

        assertEquals(message1 + message2, outputStream.toString())
    }

    @Test
    fun testWriteWithSpecialCharacters() {
        val consoleOutput = ConsoleOutput()
        val message = "Special: !@#\$%^&*()"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteWithUnicode() {
        val consoleOutput = ConsoleOutput()
        val message = "Unicode: ñáéíóú 中文 🚀"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteNumbersAsString() {
        val consoleOutput = ConsoleOutput()
        val message = "12345"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteLongString() {
        val consoleOutput = ConsoleOutput()
        val message = "A".repeat(1000)

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteWithSpaces() {
        val consoleOutput = ConsoleOutput()
        val message = "   Multiple   Spaces   "

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteWithTabs() {
        val consoleOutput = ConsoleOutput()
        val message = "Tab\tSeparated\tValues"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteUsingInfixNotation() {
        val consoleOutput = ConsoleOutput()
        val message = "Using infix notation"

        // Test que el método write puede usarse con notación infix
        consoleOutput write message

        assertEquals(message, outputStream.toString())
    }

    @Test
    fun testWriteFlushesOutput() {
        val consoleOutput = ConsoleOutput()
        val message = "Flush test"

        consoleOutput.write(message)

        // Verificar que el contenido está disponible inmediatamente después de write
        // (esto indica que se hizo flush)
        val output = outputStream.toString()
        assertEquals(message, output)
        // Si no se hubiera hecho flush, el contenido podría no estar disponible
        assert(output.isNotEmpty())
    }

    @Test
    fun testWriteMultipleMessagesSequentially() {
        val consoleOutput = ConsoleOutput()

        consoleOutput.write("Message 1")
        consoleOutput.write("Message 2")
        consoleOutput.write("Message 3")

        assertEquals("Message 1Message 2Message 3", outputStream.toString())
    }

    @Test
    fun testWriteWithFormatting() {
        val consoleOutput = ConsoleOutput()
        val message = "Formatted: %s %d"

        consoleOutput.write(message)

        assertEquals(message, outputStream.toString())
    }
}
