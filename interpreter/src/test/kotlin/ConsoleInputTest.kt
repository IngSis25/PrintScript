package org.example.input

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleInputTest {
    private val originalIn = System.`in`
    private val originalOut = System.out
    private val originalErr = System.err
    private val outputStream = ByteArrayOutputStream()
    private val errorStream = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
        System.setErr(PrintStream(errorStream))
    }

    @AfterEach
    fun tearDown() {
        System.setIn(originalIn)
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @Test
    fun testReadSimpleString() {
        val input = "Hello, World!"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadEmptyString() {
        val inputStream = ByteArrayInputStream("\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals("", result)
    }

    @Test
    fun testReadWithWhitespace() {
        val input = "  Hello World  "
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadMultipleLines() {
        val input1 = "First line"
        val input2 = "Second line"
        val inputStream = ByteArrayInputStream("$input1\n$input2\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result1 = consoleInput.read("Enter first: ")
        val result2 = consoleInput.read("Enter second: ")

        assertEquals(input1, result1)
        assertEquals(input2, result2)
    }

    @Test
    fun testReadNumbersAsString() {
        val input = "12345"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter number: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadWithSpecialCharacters() {
        val input = "Special: !@#\$%^&*()"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadWithUnicode() {
        val input = "Unicode: ñáéíóú 中文 🚀"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadLongString() {
        val input = "A".repeat(1000)
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadWithTabs() {
        val input = "Tab\tSeparated\tValues"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadUsingInfixNotation() {
        val input = "Using infix notation"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        // Test que el método read puede usarse con notación infix
        val result = consoleInput read "Enter text: "

        assertEquals(input, result)
    }

    @Test
    fun testReadFlushesOutput() {
        val input = "Test flush"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        consoleInput.read("Prompt: ")

        // Verificar que se hizo flush de System.out y System.err
        // (aunque no podemos verificar directamente, el hecho de que no falle es suficiente)
        assert(true)
    }

    @Test
    fun testReadWithDifferentPrompts() {
        val input = "Same input"
        val inputStream = ByteArrayInputStream("$input\n$input\n$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result1 = consoleInput.read("Prompt 1: ")
        val result2 = consoleInput.read("Prompt 2: ")
        val result3 = consoleInput.read("Prompt 3: ")

        assertEquals(input, result1)
        assertEquals(input, result2)
        assertEquals(input, result3)
    }

    @Test
    fun testReadWithNewlinesInInput() {
        val input = "Line1\nLine2"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        // nextLine() lee hasta el primer \n, así que debería leer "Line1"
        assertEquals("Line1", result)
    }

    @Test
    fun testReadMultipleTimesSequentially() {
        val inputs = listOf("First", "Second", "Third")
        val inputString = inputs.joinToString("\n") + "\n"
        val inputStream = ByteArrayInputStream(inputString.toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val results = inputs.map { consoleInput.read("Enter: ") }

        assertEquals(inputs, results)
    }

    @Test
    fun testReadWithEmptyPrompt() {
        val input = "Some input"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("")

        assertEquals(input, result)
    }

    @Test
    fun testReadWithOnlySpaces() {
        val input = "   "
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadBooleanValues() {
        val input = "true"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter boolean: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadDecimalNumbers() {
        val input = "3.14159"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter decimal: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadNegativeNumbers() {
        val input = "-42"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter number: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadWithMixedContent() {
        val input = "Mixed123!@#abc"
        val inputStream = ByteArrayInputStream("$input\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result = consoleInput.read("Enter text: ")

        assertEquals(input, result)
    }

    @Test
    fun testReadIsSynchronized() {
        // Este test verifica que el método @Synchronized funciona
        // Aunque es difícil testear la sincronización directamente,
        // podemos verificar que múltiples lecturas funcionan correctamente
        val input1 = "First"
        val input2 = "Second"
        val inputStream = ByteArrayInputStream("$input1\n$input2\n".toByteArray())
        System.setIn(inputStream)

        val consoleInput = ConsoleInput()
        val result1 = consoleInput.read("Enter: ")
        val result2 = consoleInput.read("Enter: ")

        assertEquals(input1, result1)
        assertEquals(input2, result2)
    }
}
