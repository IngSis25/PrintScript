package org.example.formatter.rule

// Regla que maneja las líneas en blanco antes de las declaraciones println.

// Ejemplos:
// lineBreaks = 0 → Sin líneas en blanco
// lineBreaks = 1 → Una línea en blanco (\n)
// lineBreaks = 2 → Dos líneas en blanco (\n\n)

data class LineBreaksBeforePrints(
    private val lineBreaks: Int,
) : FormatRule {
    override fun apply(): String = "\n".repeat(lineBreaks)
}
