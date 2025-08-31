package parser.rules

import ASTNode
import main.kotlin.lexer.LiteralNumber
import main.kotlin.lexer.OperatorType
import main.kotlin.lexer.Token
import main.kotlin.parser.BinaryOpNode
import main.kotlin.parser.LiteralNode
import matchers.SequenceMatcher
import matchers.TokenMatcher

class ExpressionRule : ParserRule {
    override val matcher =
        SequenceMatcher(
            listOf(
                TokenMatcher(LiteralNumber),
                TokenMatcher(OperatorType),
                TokenMatcher(LiteralNumber),
            ),
        )
}
