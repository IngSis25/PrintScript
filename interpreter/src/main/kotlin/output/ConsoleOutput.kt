package org.example.output

/**
 * Output implementation for console that writes to System.out
 */
class ConsoleOutput : Output {
    override fun write(msg: String) {
        print(msg)
        // Hacer flush para asegurar que el output se muestre inmediatamente
        // Esto es especialmente importante antes de leer input interactivo
        System.out.flush()
    }
}
