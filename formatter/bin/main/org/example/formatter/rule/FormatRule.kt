package org.example.formatter.rule

sealed interface FormatRule {
    fun apply(): String
}
