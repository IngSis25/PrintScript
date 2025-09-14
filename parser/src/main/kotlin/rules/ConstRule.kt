import builders.NodeBuilder
import matchers.MultiTypeTokenMatcher
import matchers.SequenceMatcher
import org.example.LiteralNumber
import org.example.LiteralString
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.AssignmentType
import types.IdentifierType
import types.ModifierType
import types.PunctuationType

class ConstRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(ModifierType, "const"),
                TokenMatcher(IdentifierType),
                TokenMatcher(AssignmentType),
                // el valor puede ser string o number
                MultiTypeTokenMatcher(
                    listOf(LiteralString, LiteralNumber),
                ),
                TokenMatcher(PunctuationType, ";"), // ;
            ),
        )
}
