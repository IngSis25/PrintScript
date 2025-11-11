package org.example.formatter.rule

data class InlineIfBraceRule(
    val active: Boolean,
) : FormatRule {
    override fun apply(): String = (if (active) " " else "\n") + "{\n"
}
