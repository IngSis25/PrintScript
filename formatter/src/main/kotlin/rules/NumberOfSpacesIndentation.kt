package rules

import kotlin.text.iterator

class NumberOfSpacesIndentation(
    private val indentation: Int,
) : Rule {
    override val name = "NumberOfSpacesIndentation"

    override fun applyRule(input: String): String {
        val lines = input.split("\n")
        var level = 0
        val out = StringBuilder()

        lines.forEachIndexed { idx, rawLine ->
            // Preserve empty lines
            if (rawLine.isEmpty()) {
                if (idx != lines.lastIndex) out.append("\n")
                return@forEachIndexed
            }

            var trimmed = rawLine.trimStart()

            // If line starts with closing brace, reduce level for this line
            val startsWithClosing = trimmed.startsWith("}")
            val lineIndentLevel = if (startsWithClosing) (level - 1).coerceAtLeast(0) else level

            // Build the indented line
            out.append(" ".repeat(indentation * lineIndentLevel))
            out.append(trimmed)

            if (idx != lines.lastIndex) out.append("\n")

            // Update level for next lines based on braces in this line (after accounting start-with-})
            // Count opens and closes
            val opens = trimmed.count { it == '{' }
            val closes = trimmed.count { it == '}' }
            level = (lineIndentLevel + opens - closes).coerceAtLeast(0)
        }

        return out.toString()
    }
}
