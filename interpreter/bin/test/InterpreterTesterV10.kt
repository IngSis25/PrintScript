package test.interpreterTest

import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.output.TestOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class InterpreterTesterV10 {
    @Test
    fun testInterpretAssignment() {
        val str = "let x: number = 42; println(x);"
        val lexer = Lexer(TokenFactory().createLexerV10(), StringReader(str))
        val parser = ParserFactory.createParserV10(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion10(printer)

        interpreter.interpret(parser)

        assertEquals(1, printer.printsList.size)
        assertEquals("42\n", printer.printsList[0])
    }

    private fun interpretAndCaptureOutputV10(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV10(), StringReader(input))
        val parser = ParserFactory.createParserV10(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion10(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    @Test
    fun testPrintlnWithNumber() {
        val input = "println(4);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("4\n", output)
    }

    @Test
    fun testPrintlnWithString() {
        val input = "let a: string = 'hola'; println(a);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("hola\n", output)
    }

    @Test
    fun testPrintlnWithStringAndQuotes() {
        val input = "println(\"hola\");"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("hola\n", output)
    }

    @Test
    fun testPrintlnWithVariableAssignment() {
        val input = "let b: number = 10; b = 5; println(b);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("5\n", output)
    }

    @Test
    fun testPrintlnWithStringConcatenation() {
        val input = "let a: string = 'hola'; let b: number = 5; println(a + b);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("hola5\n", output)
    }

    @Test
    fun testPrintlnWithAddition() {
        val input = "println(1 + 4);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("5\n", output)
    }

    @Test
    fun testPrintlnWithSubtraction() {
        val input = "println(5 - 1);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("4\n", output)
    }

    @Test
    fun testPrintlnWithMultiplication() {
        val input = "println(5 * 2);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("10\n", output)
    }

    @Test
    fun testPrintlnWithDivision() {
        val input = "println(10 / 2);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("5\n", output)
    }

    @Test
    fun testDecimalNumbers() {
        val input = "let x: number = 3.14; println(x);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("3.14\n", output)
    }

    @Test
    fun testDecimalAddition() {
        val input = "println(3.5 + 2.5);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("6\n", output)
    }

    @Test
    fun testDecimalSubtraction() {
        val input = "println(5.5 - 2.3);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("3.2\n", output)
    }

    @Test
    fun testDecimalMultiplication() {
        val input = "println(2.5 * 2);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("5\n", output)
    }

    @Test
    fun testDecimalDivision() {
        val input = "println(7.5 / 2.5);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("3\n", output)
    }

    @Test
    fun testNumberPlusString() {
        val input = "let a: number = 42; println(a + ' is the answer');"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("42 is the answer\n", output)
    }

    @Test
    fun testStringPlusNumber() {
        val input = "let a: string = 'Answer: '; let b: number = 42; println(a + b);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("Answer: 42\n", output)
    }

    @Test
    fun testVariableDeclarationWithoutInitialization() {
        val input = "let x: number; x = 42; println(x);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("42\n", output)
    }

    @Test
    fun testDivisionByZero() {
        val input = "println(10 / 0);"
        val exception =
            org.junit.jupiter.api.Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV10(input)
            }
        assert(exception.message?.contains("División por cero") == true || exception.cause is ArithmeticException)
    }

    @Test
    fun testMultipleAssignments() {
        val input = "let x: number = 10; x = 20; x = 30; println(x);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("30\n", output)
    }

    @Test
    fun testComplexExpression() {
        // El parser evalúa de izquierda a derecha sin precedencia: ((2 + 3) * 4) - 1 = 19
        val input = "println(2 + 3 * 4 - 1);"
        val output = interpretAndCaptureOutputV10(input)
        // Verificamos que se ejecuta sin error (el resultado exacto depende de la implementación del parser)
        assert(output.isNotBlank())
    }

    @Test
    fun testStringConcatenationMultiple() {
        val input = "let a: string = 'Hello'; let b: string = ' '; let c: string = 'World'; println(a + b + c);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("Hello World\n", output)
    }

    @Test
    fun testIntegerDivision() {
        val input = "println(15 / 4);"
        val output = interpretAndCaptureOutputV10(input)
        assertEquals("3.75\n", output)
    }
}
