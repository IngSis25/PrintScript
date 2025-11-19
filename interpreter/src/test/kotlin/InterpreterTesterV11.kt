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

    // ========== Tests para AssignmentStrategy ==========

    @Test
    fun testAssignmentWithStringValue() {
        val input = "let s: string = 'hello'; s = 'world'; println(s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("world\n", output)
    }

    @Test
    fun testAssignmentWithBooleanValue() {
        val input = "let b: boolean = true; b = false; println(b);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("false\n", output)
    }

    @Test
    fun testAssignmentWithNumberValue() {
        val input = "let n: number = 10; n = 20; println(n);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("20\n", output)
    }

    @Test
    fun testAssignmentWithExpression() {
        val input = "let x: number = 5; x = x + 10; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("15\n", output)
    }

    // ========== Tests para IdentifierStrategy ==========
    @Test
    fun testIdentifierNonExistentVariable() {
        val input = "println(x);"
        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV11(input)
            }
        assert(exception.message?.contains("Variable x no declarada") == true)
    }

    @Test
    fun testIdentifierWithStringValue() {
        val input = "let s: string = 'test'; println(s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("test\n", output)
    }

    @Test
    fun testIdentifierWithBooleanValue() {
        val input = "let b: boolean = true; println(b);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("true\n", output)
    }

    @Test
    fun testIdentifierInExpression() {
        val input = "let a: number = 5; let b: number = a + 3; println(b);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("8\n", output)
    }

    // ========== Tests para VariableDeclarationStrategy ==========

    @Test
    fun testVariableDeclarationWithNullInit() {
        val input = "let x: number; x = 42; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("42\n", output)
    }

    @Test
    fun testVariableDeclarationWithStringInit() {
        val input = "let s: string = 'initial'; println(s);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("initial\n", output)
    }

    @Test
    fun testVariableDeclarationWithBooleanInit() {
        val input = "let b: boolean = true; println(b);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("true\n", output)
    }

    // ========== Tests para BinaryExpressionStrategy - Casos de Error ==========
    @Test
    fun testBinaryExpressionUnsupportedOperation() {
        val input = "let b1: boolean = true; let b2: boolean = false; println(b1 + b2);"
        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV11(input)
            }
        assert(exception.message?.contains("Operación + no soportada") == true)
    }

    @Test
    fun testBinaryExpressionSubtractionWithString() {
        val input = "println('hello' - 'world');"
        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV11(input)
            }
        assert(exception.message?.contains("Operando izquierdo no es número") == true)
    }

    @Test
    fun testBinaryExpressionMultiplicationWithString() {
        val input = "println('hello' * 2);"
        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV11(input)
            }
        assert(exception.message?.contains("Operando izquierdo no es número") == true)
    }

    @Test
    fun testBinaryExpressionDivisionWithString() {
        val input = "println('hello' / 2);"
        val exception =
            Assertions.assertThrows(Exception::class.java) {
                interpretAndCaptureOutputV11(input)
            }
        assert(exception.message?.contains("Operando izquierdo no es número") == true)
    }

    // Nota: Algunos tests de error pueden fallar en el parser antes de llegar a la strategy
    // Estos casos se validan en el análisis semántico

    // ========== Tests para ReadInputStrategy ==========
    @Test
    fun testReadInputWithVariableMessage() {
        val code =
            """
            let prompt: string = "Enter name: ";
            let name: string = readInput(prompt);
            println(name);
            """.trimIndent()
        val testInput = TestInput()
        testInput.addInput("Alice")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter name: Alice\n", output)
    }

    @Test
    fun testReadInputTransformToBooleanTrue() {
        val code = "let b: boolean = readInput('Enter: '); println(b);"
        val testInput = TestInput()
        testInput.addInput("true")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter: true\n", output)
    }

    @Test
    fun testReadInputTransformToBooleanFalse() {
        val code = "let b: boolean = readInput('Enter: '); println(b);"
        val testInput = TestInput()
        testInput.addInput("false")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter: false\n", output)
    }

    @Test
    fun testReadInputTransformToInteger() {
        val code = "let n: number = readInput('Enter: '); println(n);"
        val testInput = TestInput()
        testInput.addInput("42")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter: 42\n", output)
    }

    @Test
    fun testReadInputTransformToDouble() {
        val code = "let n: number = readInput('Enter: '); println(n);"
        val testInput = TestInput()
        testInput.addInput("3.14")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter: 3.14\n", output)
    }

    @Test
    fun testReadInputTransformToNegativeNumber() {
        val code = "let n: number = readInput('Enter: '); println(n);"
        val testInput = TestInput()
        testInput.addInput("-10")

        val output = interpretAndCaptureOutputV11WithInput(code, testInput)
        assertEquals("Enter: -10\n", output)
    }

    // ========== Tests para ReadEnvStrategy ==========
    @Test
    fun testReadEnvNonExistentVariable() {
        val input = "let value: string = readEnv('NON_EXISTENT'); println(value);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("\n", output) // Devuelve string vacío
    }

    @Test
    fun testReadEnvFromContext() {
        // Primero establecer en context, luego leer con readEnv
        val input =
            """
            let gravity: number = 9.81;
            let g: string = readEnv('gravity');
            println(g);
            """.trimIndent()
        val output = interpretAndCaptureOutputV11(input)
        // Debería leer del context si no existe en Environment
        assert(output.contains("9.81"))
    }

    // ========== Tests para PrintStatementStrategy ==========
    @Test
    fun testPrintNullValue() {
        val input = "let x: number; println(x);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("null\n", output)
    }

    @Test
    fun testPrintBooleanTrue() {
        val input = "println(true);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("true\n", output)
    }

    @Test
    fun testPrintBooleanFalse() {
        val input = "println(false);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("false\n", output)
    }

    @Test
    fun testPrintIntegerAsInteger() {
        val input = "println(42);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("42\n", output) // Debería mostrarse sin decimales
    }

    @Test
    fun testPrintDoubleAsDouble() {
        val input = "println(3.14);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("3.14\n", output)
    }

    @Test
    fun testPrintString() {
        val input = "println('hello');"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("hello\n", output)
    }

    @Test
    fun testPrintExpressionResult() {
        val input = "println(10 + 20);"
        val output = interpretAndCaptureOutputV11(input)
        assertEquals("30\n", output)
    }

    // ========== Tests adicionales para BinaryExpressionStrategy ==========
    // Nota: Algunos tests de comparaciones con variables pueden fallar en el parser
    // Los casos básicos ya están cubiertos en otros tests

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
}
