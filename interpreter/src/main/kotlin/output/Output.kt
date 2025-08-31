package org.example.output

/** Abstracción de salida (consola, buffer, etc). */
interface Output {
    infix fun write(msg: String)
}
