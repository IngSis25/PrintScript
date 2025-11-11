package rules

class AddTwoSpacesAfterOpenBrace : Rule {
    override val name: String = "AddTwoSpacesAfterOpenBrace"

    override fun applyRule(input: String): String {
        // Insert two spaces at the start of the line following an opening brace if content starts immediately
        // Handles both cases:
        // if (cond){\nprintln(...)  -> if (cond){\n  println(...)
        // if (cond)\n{\nprintln(...) -> if (cond)\n{\n  println(...)
        return input
            .replace(Regex("\\{\\n(\\S)"), "{\n  $1")
    }
}
