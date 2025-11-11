package org.example.formatter.rule

data class LineBreaksRule(
    private val lineBreaks: Int,
) : FormatRule {
    override fun apply(): String = "\n".repeat(lineBreaks)
}
