package org.example.input

import java.util.Scanner

class ConsoleInput : Input {
    // Usar un único Scanner compartido para evitar problemas con múltiples lecturas
    // Scanner es más robusto para entrada interactiva que BufferedReader
    private val scanner = Scanner(System.`in`)

    @Synchronized
    override fun read(message: String): String {
        // Asegurar que el output se muestre antes de leer
        System.out.flush()
        System.err.flush()

        // Pequeña pausa para asegurar que el output se haya enviado completamente
        // Esto es especialmente importante cuando se ejecuta a través de Gradle
        try {
            Thread.sleep(50) // Aumentado a 50ms para dar más tiempo
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        // Intentar usar System.console() primero (más confiable para entrada interactiva)
        val console = System.console()
        if (console != null) {
            return console.readLine() ?: ""
        }

        // Si System.console() no está disponible, usar Scanner con System.in
        // nextLine() bloqueará hasta que el usuario ingrese una línea y presione Enter
        return try {
            // nextLine() bloquea automáticamente hasta que haya entrada disponible
            scanner.nextLine()
        } catch (e: Exception) {
            // Si hay un error de lectura (por ejemplo, stream cerrado), devolver cadena vacía
            ""
        }
    }
}
