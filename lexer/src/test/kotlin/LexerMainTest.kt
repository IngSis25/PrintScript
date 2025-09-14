package main.kotlin.lexer

import main
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LexerMainTest {
    @Test
    fun testMain() {
        // Capturamos la salida de consola
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        try {
            main(arrayOf("1.0"))
        } finally {
            System.setOut(originalOut)
        }

        val output = outputStream.toString()
        assert(output.contains("x"))
        assert(output.contains("2"))
    }
}
