import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.output.TestOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class VariableTests {
    private fun interpretAndCaptureOutputV11(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    @Test
    fun testVariableReuse() {
        val input =
            """
            let x: number = 10;
            println(x);
            let y: number = x + 5;
            println(y);
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("10\n15\n", output)
    }

    @Test
    fun testMultipleVariableDeclarations() {
        val input =
            """
            let a: number = 1;
            let b: number = 2;
            let c: number = 3;
            println(a + b + c);
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("6\n", output)
    }

    @Test
    fun testConstVariable() {
        val input = "const x: number = 42; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("42\n", output)
    }

    @Test
    fun testConstString() {
        val input = "const s: string = 'constant'; println(s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("constant\n", output)
    }

    // ========== Tests de ReadEnv ==========
    @Test
    fun testReadEnvNumber() {
        val input = "let g: number = readEnv('gravity'); println(g);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("9.81\n", output)
    }

    @Test
    fun testReadEnvInExpression() {
        val input = "let g: number = readEnv('gravity'); let add: number = 0.19; let result: number = g + add; println(result);"
        val output = interpretAndCaptureOutputV11(input)
        // 9.81 + 0.19 = 10.0
        assert(output.contains("10"))
    }

    // ========== Tests de If/Else ==========

    // ========== Tests de Casos Edge ==========
    @Test
    fun testZeroOperations() {
        val input = "println(0 + 0);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("0\n", output)
    }

    @Test
    fun testLargeNumbers() {
        val input = "println(1000 * 1000);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("1000000\n", output)
    }

    @Test
    fun testDecimalPrecision() {
        val input = "println(0.1 + 0.2);"
        val output = interpretAndCaptureOutputV11(input)
        assert(output.contains("0.3"))
    }

    @Test
    fun testEmptyString() {
        val input = "let s: string = ''; println('Empty: ' + s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Empty: \n", output)
    }

    @Test
    fun testStringWithSpaces() {
        val input = "let s: string = 'hello world'; println(s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("hello world\n", output)
    }
}
