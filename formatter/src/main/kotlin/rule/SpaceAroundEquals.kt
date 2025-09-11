
// Regla que maneja los espacios alrededor del operador de asignación "=".

// Ejemplos:
// spaceAround = true  → " = " (con espacios)
// spaceAround = false → "="   (sin espacios)

data class SpaceAroundEquals(
    private val spaceAround: Boolean,
) : FormatRule {
    override fun apply(): String =
        if (spaceAround) {
            " = " // Con espacios: let x = 5
        } else {
            "=" // Sin espacios: let x=5
        }
}
