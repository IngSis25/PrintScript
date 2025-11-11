package rules

class NewLineAfterSemiColon : Rule {
    override val name: String = "NewLineAfterSemiColon"

    override fun applyRule(input: String): String = input.replace(";", ";\n")
}
