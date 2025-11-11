package rules

class SameLineForElseAndBrace : Rule {
    override val name: String = "SameLineForElseAndBrace"

    override fun applyRule(input: String): String = input.replace(Regex("}\\s*\\n\\s*else"), "} else")
}
