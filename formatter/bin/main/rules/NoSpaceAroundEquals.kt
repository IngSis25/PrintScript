package rules

class NoSpaceAroundEquals : Rule {
    override val name: String = "NoSpaceAroundEquals"

    override fun applyRule(input: String): String {
        var out = input.replace(Regex("\\s*(=)\\s*"), "$1")
        // Asegurar un espacio después de ':' cuando va seguido de un tipo/identificador (declaraciones)
        out = out.replace(Regex(":\\s*([A-Za-z_])"), ": $1")
        return out
    }
}
