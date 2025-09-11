package io.printscript.rule

// Ejemplo: SpaceAroundEquals devuelve " = " o "="

sealed interface FormatRule {
    fun apply(): String
}
