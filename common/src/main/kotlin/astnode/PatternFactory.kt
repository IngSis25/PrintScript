package org.example.astnode

class PatternFactory {
    companion object {
        fun getBinaryExpressionPattern(): String =
            "(IdentifierToken|StringToken|NumberToken)" +
                "(\\s*(PlusToken|MinusToken|MultiplyToken|DivisionToken)\\s*" +
                "(IdentifierToken|StringToken|NumberToken))*"

        fun getBooleanExpressionPattern(): String = "(BooleanToken|IdentifierToken)"

        fun getExpressionPattern(): String {
            val binaryExpressionPattern = getBinaryExpressionPattern()
            val booleanExpressionPattern = getBooleanExpressionPattern()
            val inputExpressionPattern = getReadInputPattern()
            val environmentExpressionPattern = getReadEnvironmentPattern()
            return "($binaryExpressionPattern" +
                "|$booleanExpressionPattern" +
                "|$inputExpressionPattern" +
                "|$environmentExpressionPattern)"
        }

        fun getIfWithElsePattern(): String =
            "^IfToken\\s+OpenParenthesisToken\\s+BooleanToken\\s+" +
                "CloseParenthesisToken\\s+OpenBraceToken\\s+.*?\\s+" +
                "CloseBraceToken\\s+ElseToken\\s+OpenBraceToken\\s+.*?\\s+CloseBraceToken$"

        fun getReadInputPattern(): String {
            // ReadInputToken ( ExpressionNode )
            val binaryExpressionPattern = getBinaryExpressionPattern()
            val readInputPattern =
                "(ReadInputToken\\s+OpenParenthesisToken\\s+" +
                    "($binaryExpressionPattern)\\s+CloseParenthesisToken)"
            return "(ReadInputToken\\s+OpenParenthesisToken\\s+" +
                "($binaryExpressionPattern|$readInputPattern)\\s+CloseParenthesisToken)"
        }

        fun getReadEnvironmentPattern(): String =
            "(ReadEnvironmentToken\\s+OpenParenthesisToken\\s+" +
                "StringToken\\s+CloseParenthesisToken)"

        fun getNamingFormatPattern(namingPatternName: String): String =
            when (namingPatternName) {
                "camelCase" -> {
                    // starts with a lowercase letter and can include alphanumeric characters without requiring uppercase segments
                    """^[a-z][a-zA-Z0-9]*$"""
                }
                "snake_case" -> {
                    // starts with a lowercase letter, followed by any number of letters or numbers, and then an underscore, followed by any number of letters or numbers.
                    """^[a-z][a-zA-Z0-9]*_[a-zA-Z0-9]*$"""
                }
                else -> {
                    throw IllegalArgumentException("Invalid naming pattern name")
                }
            }
    }
}
