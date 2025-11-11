package rules

class SpaceBeforeColon : Rule {
    override val name = "space_before_colon"

    override fun applyRule(input: String): String {
        // Asegura exactamente un espacio antes de ':' y preserva si en el original había espacio después
        val original = FormattingContext.originalLine
        val hadSpaceAfterColon =
            original?.contains(Regex("^\\s*(let|const)\\b.*:\\s")) == true

        var out = input.replace(Regex("\\s*:"), " :")
        out =
            if (hadSpaceAfterColon) {
                out.replace(Regex(" :\\s*"), " : ")
            } else {
                out.replace(Regex(" :\\s*"), " :")
            }
        return out
    }
}
