package rules

class NoSpaceAroundEquals : Rule {
    override val name: String = "NoSpaceAroundEquals"

    override fun applyRule(input: String): String = input.replace(Regex("\\s*(=)\\s*"), "$1")
}
