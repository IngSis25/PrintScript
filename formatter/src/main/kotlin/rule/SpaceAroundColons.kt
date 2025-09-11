package org.example.rule

// Regla que maneja los espacios alrededor de los dos puntos ":" en declaraciones

// Ejemplos:
// spaceBefore=false, spaceAfter=true  → ": string"
// spaceBefore=true,  spaceAfter=false → " :"
// spaceBefore=true,  spaceAfter=true  → " : "
// spaceBefore=false, spaceAfter=false → ":"

data class SpaceAroundColons(
    private val spaceBefore: Boolean,
    private val spaceAfter: Boolean,
) : FormatRule {
    override fun apply(): String {
        val before = if (spaceBefore) " " else ""
        val after = if (spaceAfter) " " else ""
        return "$before:$after"
    }
}
