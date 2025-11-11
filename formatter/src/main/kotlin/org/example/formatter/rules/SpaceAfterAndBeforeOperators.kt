package rules

class SpaceAfterAndBeforeOperators : Rule {
    override val name: String = "SpaceAfterAndBeforeOperators"

    override fun applyRule(input: String): String = input.replace(Regex("\\s*([+\\-*/])\\s*"), " $1 ")
}
