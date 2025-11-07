package lexer

class Location(
    private val line: Int,
    private val column: Int,
) {
    override fun toString(): String = "(line: $line, column: $column)"
}
