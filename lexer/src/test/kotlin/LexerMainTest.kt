package main.kotlin.lexer

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
            main() // Llamamos a tu función main
        } finally {
            System.setOut(originalOut) // Restauramos la salida original
        }

        // Opcional: podés verificar que se generaron tokens
        val output = outputStream.toString()
        assert(output.contains("x")) // verifica que el token 'x' apareció
        assert(output.contains("2")) // verifica que el token '2' apareció
    }
}
