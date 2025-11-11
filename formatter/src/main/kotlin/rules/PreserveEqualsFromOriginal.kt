package rules

class PreserveEqualsFromOriginal : Rule {
    override val name: String = "PreserveEqualsFromOriginal"

    override fun applyRule(input: String): String {
        val original = FormattingContext.originalLine ?: return input

        // Solo para declaraciones/assigns (heurística sencilla: empieza con let|const o contiene =)
        if (!original.contains("=")) return input

        val originalNoSpaces = original.contains(Regex("=[^\\s]")) || original.contains(Regex("[^\\s]="))
        val originalWithSpaces = original.contains(Regex("\\s=\\s"))

        return when {
            originalWithSpaces -> input.replace(Regex("\\s*(=)\\s*"), " $1 ")
            originalNoSpaces -> input.replace(Regex("\\s*(=)\\s*"), "$1")
            else -> input
        }
    }
}
