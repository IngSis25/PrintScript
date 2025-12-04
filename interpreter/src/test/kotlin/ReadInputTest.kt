import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.input.TestInput
import org.example.output.TestOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class ReadInputTest {
    private fun interpretAndCaptureOutputV11WithInput(
        code: String,
        testInput: TestInput,
    ): String {
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer, testInput)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    private fun interpretAndCaptureOutputV11(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    @Test
    fun testReadInputWithNumberConversion() {
        // readInput devuelve string, pero el interpreter lo convierte automáticamente
        val code = "let numStr: string = readInput(\"Enter number:\"); println(numStr);"
        val testInput = TestInput()
        testInput.addInput("5")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter number:5\n", output)
    }

    @Test
    fun testReadInputWithDouble() {
        val code = "let value: string = readInput(\"Enter value:\"); println(value);"
        val testInput = TestInput()
        testInput.addInput("3.14")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter value:3.14\n", output)
    }

    @Test
    fun testReadInputWithNumberType() {
        // Test que verifica que readInput puede asignarse a una variable de tipo number
        // y que el intérprete convierte automáticamente el string a number
        val code =
            """
            let a: number = readInput("Enter a number: ");
            println(a);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("42")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        // Debería mostrar el prompt, leer "42", convertirlo a número y mostrarlo
        assertEquals("Enter a number: 42\n", output)
    }

    @Test
    fun testReadInputWithBooleanType() {
        // Test que verifica que readInput puede asignarse a una variable de tipo boolean
        // y que el intérprete convierte automáticamente el string a boolean
        val code =
            """
            let flag: boolean = readInput("Enter true or false: ");
            println(flag);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("true")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        // Debería mostrar el prompt, leer "true", convertirlo a boolean y mostrarlo
        assertEquals("Enter true or false: true\n", output)
    }

    @Test
    fun testReadInputWithNegativeNumber() {
        // Test que verifica que readInput funciona con números negativos
        val code =
            """
            let a: number = readInput("Enter a number: ");
            println(a);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("-10")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter a number: -10\n", output)
    }

    @Test
    fun testReadInputWithDecimalNumber() {
        // Test que verifica que readInput funciona con números decimales
        val code =
            """
            let a: number = readInput("Enter a decimal: ");
            println(a);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("3.14159")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter a decimal: 3.14159\n", output)
    }

    @Test
    fun testReadInputWithStringType() {
        // Test que verifica que readInput también funciona con tipo string (sin conversión)
        val code =
            """
            let name: string = readInput("Enter your name: ");
            println("Hello, " + name + "!");
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("Alice")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter your name: Hello, Alice!\n", output)
    }

    @Test
    fun testReadInputWithConst() {
        // Test que verifica que readInput funciona con const
        val code =
            """
            const value: number = readInput("Enter a constant value: ");
            println(value);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("100")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter a constant value: 100\n", output)
    }

    @Test
    fun testReadInputWithBooleanFalse() {
        // Test que verifica conversión a boolean false
        val code =
            """
            let flag: boolean = readInput("Enter boolean: ");
            println(flag);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("false")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter boolean: false\n", output)
    }

    @Test
    fun testReadInputWithStringThatCannotConvertToNumber() {
        // Test que verifica que si se lee un string que no es número, se mantiene como string
        val code =
            """
            let a: number = readInput("Enter a number: ");
            println(a);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("abc")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        // Si no se puede convertir a número, debería mantenerse como string "abc"
        assertEquals("Enter a number: abc\n", output)
    }

    @Test
    fun testReadInputWithZero() {
        // Test que verifica que readInput funciona con el número 0
        val code =
            """
            let a: number = readInput("Enter zero: ");
            println(a);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("0")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter zero: 0\n", output)
    }

    @Test
    fun testComplexBooleanExpression() {
        val input =
            """
            let x: boolean = true;
            let y: boolean = false;
            if (x) {
                if (y) {
                    println('Both true');
                } else {
                    println('Only x true');
                }
            }
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Only x true\n", output)
    }
}
