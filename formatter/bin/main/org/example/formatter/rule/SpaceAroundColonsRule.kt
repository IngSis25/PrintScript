package org.example.formatter.rule

// Regla que maneja los espacios alrededor de los dos puntos ":" en declaraciones

// Ejemplos:
// spaceBefore=false, spaceAfter=true  → ": string"
// spaceBefore=true,  spaceAfter=false → " :"
// spaceBefore=true,  spaceAfter=true  → " : "
// spaceBefore=false, spaceAfter=false → ":"

data class SpaceAroundColonsRule(
    private val before: Boolean,
    private val after: Boolean,
) : FormatRule {
    override fun apply(): String {
        val result = StringBuilder()
        if (before) {
            result.append(" ")
        }
        result.append(":")
        if (after) {
            result.append(" ")
        }
        return result.toString()
    }
}
