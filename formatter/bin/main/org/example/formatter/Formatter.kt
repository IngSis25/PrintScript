package org.example.formatter

import org.example.astnode.ASTNode
import org.example.formatter.config.FormatterConfig

class Formatter(
    private val config: FormatterConfig,
) {
    fun format(asts: Iterator<ASTNode>): String {
        val result = StringBuilder()
        val visitor = FormatterVisitor(config, result)
        while (asts.hasNext()) {
            val node = asts.next()
            visitor.visit(node)
        }
        return result.toString()
    }

    init { // validate config
        config.indent?.takeIf { it < 0 }?.let {
            throw IllegalParameterException("\"indent-inside-if\" must be greater than or equal to 0, $it was provided")
        }
        if (config.ifBraceSameLine == config.ifBraceBelowLine && config.ifBraceSameLine != null) {
            throw IllegalParameterException("\"if-brace-same-line\" and \"if-brace-below-line\" should be different")
        }
        if (config.spaceAroundEquals == config.noSpaceAroundEquals && config.spaceAroundEquals != null) {
            throw IllegalParameterException(
                "\"enforce-spacing-around-equals\" " +
                    "and \"enforce-no-spacing-around-equals\" should be different",
            )
        }
        config.lineBreaksAfterPrints?.takeIf { it < 0 || it > 2 }?.let {
            throw IllegalParameterException("\"line-breaks-after-println\" must be between 0 and 2, $it was provided")
        }
    }
}
