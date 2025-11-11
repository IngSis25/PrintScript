package rules

class OnlyOneSpacePermited : Rule {
    override val name: String = "OnlyOneSpacePermited"

    override fun applyRule(input: String): String {
        // Colapsar múltiples espacios solo cuando NO son indentación al inicio de línea
        // Preservar la indentación (p. ej., dos espacios dentro de bloques)
        return input.replace(Regex("(?m)(?<=\\S)[ \\t]{2,}"), " ")
    }
}
