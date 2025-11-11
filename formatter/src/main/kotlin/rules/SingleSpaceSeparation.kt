package rules

class SingleSpaceSeparation : Rule {
    override val name: String = "SingleSpaceSeparation"

    override fun applyRule(input: String): String {
        var out = input

        // Add spaces around colon and equals
        out = out.replace(Regex("\\s*(:)\\s*"), " $1 ")
        out = out.replace(Regex("\\s*(=)\\s*"), " $1 ")

        // Space before '(' when preceded by an identifier or keyword
        out = out.replace(Regex("(\\w)\\("), "$1 (")

        // Space after '(' when there is non-empty content
        out = out.replace(Regex("\\((\\S)"), "( $1")

        // Space before ')' when there is non-empty content
        out = out.replace(Regex("(\\S)\\)"), "$1 )")

        // Normalize multiple spaces
        out = out.replace(Regex("[ \\t]{2,}"), " ")

        return out
    }
}
