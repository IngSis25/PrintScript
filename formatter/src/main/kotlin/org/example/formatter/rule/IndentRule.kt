package org.example.formatter.rule

data class IndentRule(
    val indentSize: Int,
) : FormatRule {
    override fun apply(): String = " ".repeat(indentSize)
}
