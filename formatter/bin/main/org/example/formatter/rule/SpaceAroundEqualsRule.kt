package org.example.formatter.rule

// Regla que maneja los espacios alrededor del operador de asignación "=".

// Ejemplos:
// spaceAround = true  → " = " (con espacios)
// spaceAround = false → "="   (sin espacios)

data class SpaceAroundEqualsRule(
    private val active: Boolean,
) : FormatRule {
    override fun apply(): String {
        val result = StringBuilder()
        if (active) {
            result.append(" ")
        }
        result.append("=")
        if (active) {
            result.append(" ")
        }
        return result.toString()
    }
}
