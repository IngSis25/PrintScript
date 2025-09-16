package org.example.formatter.config

import org.example.formatter.rule.LineBreaksBeforePrints
import org.example.formatter.rule.SpaceAroundColons
import org.example.formatter.rule.SpaceAroundEquals
import org.example.formatter.rule.SpaceInsideParentheses

// Configuración principal del formatter que se carga desde JSON
// todas las opciones de formateo y crea las reglas correspondientes

data class FormatterConfig(
    val lineBreaksBeforePrints: Int = 0,
    val spaceAroundEquals: Boolean = true,
    val spaceBeforeColon: Boolean = false,
    val spaceAfterColon: Boolean = false,
    val spaceAroundAssignment: Boolean = true,
    val spaceInsideParentheses: Boolean = false,
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

    fun spaceAroundAssignmentRule(): SpaceAroundEquals = SpaceAroundEquals(spaceAroundAssignment)

    fun spaceInsideParenthesesRule(): SpaceInsideParentheses = SpaceInsideParentheses(spaceInsideParentheses)
}
