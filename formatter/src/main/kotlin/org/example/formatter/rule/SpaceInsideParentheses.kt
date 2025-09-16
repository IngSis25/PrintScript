package org.example.formatter.rule

// Regla que maneja los espacios dentro de los paréntesis en llamadas a funciones
// Ejemplos:
// spaceInside=false → "println(value)"
// spaceInside=true  → "println( value )"

data class SpaceInsideParentheses(
    val spaceInside: Boolean,
) : FormatRule {
    fun applyOpen(): String = if (spaceInside) "( " else "("

    fun applyClose(): String = if (spaceInside) " )" else ")"

    override fun apply(): String {
        // This method is required by FormatRule but not used for this rule
        return if (spaceInside) "( )" else "()"
    }
}
