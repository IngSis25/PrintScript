package rules

class SpaceAfterColon : Rule {
    override val name: String = "SpaceAfterColon"

    override fun applyRule(input: String): String {
        // Ensure exactly one space AFTER colon, keep whatever is BEFORE.
        var out = input.replace(Regex(":\\s*"), ": ")

        // If the original line had a space BEFORE ':', preserve it (only for declarations)
        val original = FormattingContext.originalLine
        val hadSpaceBeforeColon =
            original?.contains(Regex("^\\s*(let|const)\\b.*\\s:")) == true
        if (hadSpaceBeforeColon) {
            out = out.replace(Regex("([^\\s]):\\s"), "$1 : ")
        }
        return out
    }
}
