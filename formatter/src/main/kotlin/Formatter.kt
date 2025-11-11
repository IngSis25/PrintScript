package org.example.formatter
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.iterator.PrintScriptIterator
import rules.FormattingContext
import rules.Rule

class Formatter(
    private val nodeIterator: PrintScriptIterator<ASTNode>,
    private val originalSource: String? = null,
) {
    private val visitor: ASTNodeVisitor = FormatterVisitor()

    fun format(rules: List<Rule>): FormatResult {
        val code: MutableList<String> = mutableListOf()
        var result = ""

        // Takes each AST and gets its string representation
        while (nodeIterator.hasNext()) {
            val node = nodeIterator.next()!!
            code.add(visitor.visit(node).toString())
        }

        // Applies rules to each statement of code
        // If originalSource is provided, split it by semicolons to get original statements
        val originalStatements: List<String>? =
            originalSource
                ?.split(Regex("(?<=;)\\s*"))
                ?.filter { it.isNotBlank() }

        code.forEachIndexed { index, lineFromVisitor ->
            // Set original line for context-aware rules
            FormattingContext.originalLine = originalStatements?.getOrNull(index)
            val trimmed = lineFromVisitor.trimStart()
            val useOriginal =
                originalStatements != null &&
                    (trimmed.startsWith("let ") || trimmed.startsWith("println("))
            val baseLine =
                if (useOriginal) {
                    originalStatements?.getOrNull(index) ?: lineFromVisitor
                } else {
                    lineFromVisitor
                }
            result += applyRules(baseLine, rules)
        }

        return FormatResult(result)
    }

    private fun applyRules(
        line: String,
        rules: List<Rule>,
    ): String {
        var modifiedLine = line
        rules.forEach { rule ->
            modifiedLine = rule.applyRule(modifiedLine)
        }
        return modifiedLine
    }
}
