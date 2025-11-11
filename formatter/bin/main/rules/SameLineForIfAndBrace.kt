package rules

import kotlin.text.Regex

class SameLineForIfAndBrace : Rule {
    override val name: String = "SameLineForIfAndBrace"

    override fun applyRule(input: String): String =
        input.replace(Regex("\\bif\\b\\s*(\\(.*?\\))\\s*\\n\\s*\\{"), "if $1 {")
}
