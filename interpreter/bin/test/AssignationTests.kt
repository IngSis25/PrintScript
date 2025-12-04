import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.output.TestOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class AssignationTests {
    private fun interpretAndCaptureOutputV11(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    @Test
    fun testMultipleAssignments() {
        val input = "let x: number = 1; x = 2; x = 3; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("3\n", output)
    }

    @Test
    fun testAssignmentWithExpression() {
        val input = "let x: number = 5; x = x + 5; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("10\n", output)
    }

    @Test
    fun testAssignmentString() {
        val input = "let s: string = 'hello'; s = 'world'; println(s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("world\n", output)
    }

    @Test
    fun testAssignmentBoolean() {
        val input = "let b: boolean = true; b = false; println(b);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("false\n", output)
    }
}
