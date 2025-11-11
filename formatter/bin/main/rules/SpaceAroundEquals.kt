package rules

class SpaceAroundEquals : Rule {
    override val name = "space_around_equals"

    override fun applyRule(input: String): String {
        var out = input.replace(Regex("\\s*(=)\\s*"), " $1 ")
        // Asegurar un espacio después de ':' cuando va seguido de un identificador/tipo (declaraciones)
        out = out.replace(Regex(":\\s*([A-Za-z_])"), ": $1")
        return out
    }
}
