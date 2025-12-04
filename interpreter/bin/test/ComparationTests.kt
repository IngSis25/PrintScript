import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.output.TestOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class ComparationTests {
    private fun interpretAndCaptureOutputV11(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    @Test
    fun testMultipleAdditions() {
        val input = "println(1 + 2 + 3);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("6\n", output)
    }

    @Test
    fun testSubtractionWithNegativeResult() {
        val input = "println(5 - 10);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("-5\n", output)
    }

    @Test
    fun testMultiplicationWithDecimals() {
        val input = "println(2.5 * 3);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("7.5\n", output)
    }

    @Test
    fun testDivisionWithDecimals() {
        val input = "println(7.5 / 2.5);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("3\n", output)
    }

    @Test
    fun testStringPlusNumber() {
        val input = "println('Number: ' + 42);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Number: 42\n", output)
    }

    @Test
    fun testNumberPlusStringAgain() {
        val input = "println(42 + ' is the answer');"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("42 is the answer\n", output)
    }

    @Test
    fun testStringConcatenationMultiple() {
        val input = "println('Hello' + ' ' + 'World');"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Hello World\n", output)
    }
}
