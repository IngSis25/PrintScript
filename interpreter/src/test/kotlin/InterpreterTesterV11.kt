package test.interpreterTest

import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenFactory
import org.ParserFactory
import org.example.InterpreterFactory
import org.example.input.TestInput
import org.example.output.TestOutput
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringReader

class InterpreterTesterV11 {
    private fun interpretAndCaptureOutputV11(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

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

    private fun interpretAndCaptureOutputV10(input: String): String {
        val lexer = Lexer(TokenFactory().createLexerV10(), StringReader(input))
        val parser = ParserFactory.createParserV10(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion10(printer)

        interpreter.interpret(parser)

        return printer.printsList.joinToString(separator = "")
    }

    @Test
    fun testInterpretAssignment() {
        val str = "let x: number = 42; println(x);"
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(str))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        interpreter.interpret(parser)

        assertEquals(1, printer.printsList.size)
        assertEquals("42\n", printer.printsList[0])
    }

    @Test
    fun testInterpretAssignmentWithoutSemicolon() {
        val str = "let x: number = 42; println(x)"
        val lexer = Lexer(TokenFactory().createLexerV11(), StringReader(str))
        val parser = ParserFactory.createParserV11(lexer)
        val printer = TestOutput()
        val interpreter = InterpreterFactory.createInterpreterVersion11(printer)

        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpreter.interpret(parser)
            }

        assertEquals(
            "Unexpected end of input. Missing semicolon or brace at the end of the file.",
            exception.message,
        )
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
    fun testIfNode() {
        val input =
            """
            if (true) {
                println('Hello');
            }
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Hello\n", output)
    }

    @Test
    fun testIfCompleteNode() {
        val input =
            """
            if (false) {
                println('Hello');
            } else {
                println('World');
            }
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("World\n", output)
    }

    @Test
    fun testIfNestedNode() {
        val input =
            """
            if (true) {
                if (true) {
                    println('Hello');
                    if (true) {
                        println('World');
                    }
                } else {
                    println('Goodbye World');
                }
            }
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Hello\nWorld\n", output)
    }

    @Test
    fun testIfElseNode() {
        val input =
            """
            const booleanResult: boolean = true;
            if(booleanResult) {
                println("else statement working correctly");
            } else {
                println("else statement not working correctly");
            }
            println("outside of conditional");
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("else statement working correctly\noutside of conditional\n", output)
    }

    @Test
    fun testReadInput() {
        val code =
            """
            const name: string = readInput("Name:");
            println("Hello " + name + "!");
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("world")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)

        // El prompt "Name:" se imprime sin salto de línea, luego se lee "world",
        // y luego se imprime "Hello world!" con salto de línea
        assertEquals("Name:Hello world!\n", output)
    }

    @Test
    fun testReadInputWithNewline() {
        // Test que verifica que readInput imprime el prompt y lee correctamente
        val code = "const name: string = readInput(\"Name:\");\nprintln(\"Hello \" + name + \"!\");"
        val testInput = TestInput()
        testInput.addInput("world")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)

        // Verificamos que name contiene "world" y no "Name:"
        assert(output.contains("Hello world!"))
        assert(!output.contains("Hello Name:!"))
    }

    @Test
    fun testReadEnv() {
        val input = "const gravity: number = readEnv('gravity'); println(gravity);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("9.81\n", output)
    }

    @Test
    fun testReadEnvPi() {
        val input = "const piValue: number = readEnv('pi'); println(piValue);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("3.1415\n", output)
    }

    @Test
    fun testReadEnvString() {
        val input = "const club: string = readEnv('BEST_FOOTBALL_CLUB'); println(club);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("San Lorenzo\n", output)
    }

    @Test
    fun testVariableDeclarationWithoutInitialization() {
        val input = "let x: number; x = 42; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("42\n", output)
    }

    @Test
    fun testDivisionByZero() {
        val input = "println(10 / 0);"
        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV11(input)
            }
        assert(exception.message?.contains("División por cero") == true || exception.cause is ArithmeticException)
    }

    @Test
    fun testDecimalNumbers() {
        val input = "let x: number = 3.14; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("3.14\n", output)
    }

    @Test
    fun testDecimalOperations() {
        val input = "println(3.5 + 2.5);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("6\n", output)
    }

    @Test
    fun testDecimalMultiplication() {
        val input = "println(2.5 * 2);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("5\n", output)
    }

    @Test
    fun testNumberPlusString() {
        val input = "let a: number = 42; println(a + ' is the answer');"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("42 is the answer\n", output)
    }

    @Test
    fun testIfWithBooleanVariableFalse() {
        val input =
            """
            let flag: boolean = false;
            if (flag) {
                println('Should not print');
            } else {
                println('Should print');
            }
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("Should print\n", output)
    }

    @Test
    fun testBooleanExpressionWithVariable() {
        val input =
            """
            let x: boolean = true;
            if (x) {
                println('True branch');
            } else {
                println('False branch');
            }
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("True branch\n", output)
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
