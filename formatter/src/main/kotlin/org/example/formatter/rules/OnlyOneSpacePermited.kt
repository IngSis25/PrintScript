package rules

class OnlyOneSpacePermited : Rule {
    override val name: String = "OnlyOneSpacePermited"

    override fun applyRule(input: String): String = input.replace(Regex("[ \\t]+\n"), " ")
}
