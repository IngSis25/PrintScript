

// Configuración principal del formatter que se carga desde JSON
// todas las opciones de formateo y crea las reglas correspondientes

data class FormatterConfig(
    val lineBreaksBeforePrints: Int,
    val spaceAroundEquals: Boolean,
    val spaceBeforeColon: Boolean,
    val spaceAfterColon: Boolean,
) {
    init {
        // lineBreaksBeforePrints debe estar entre 0 y 2 -> valid
        if (lineBreaksBeforePrints !in 0..2) {
            throw IllegalArgumentException(
                "lineBreaksBeforePrints debe estar entre 0 y 2, pero era: $lineBreaksBeforePrints",
            )
        }
    }

    // Factory methods

    fun lineBreaksBeforePrintsRule(): LineBreaksBeforePrints = LineBreaksBeforePrints(lineBreaksBeforePrints)

    fun spaceAroundEqualsRule(): SpaceAroundEquals = SpaceAroundEquals(spaceAroundEquals)

    fun spaceAroundColonsRule(): SpaceAroundColons = SpaceAroundColons(spaceBeforeColon, spaceAfterColon)
}
