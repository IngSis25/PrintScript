package test.parserTest

import builders.BlockBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import org.example.ast.BlockNode
import org.junit.jupiter.api.Test
import rules.RuleMatcher
import types.AssignmentType
import types.IdentifierType
import types.ModifierType
import types.PunctuationType
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlockBuilderTests {
    val ruleMatcher = RuleMatcher(ConfiguredRules.V1)
    val parser = DefaultParser(ruleMatcher)

    private val blockBuilder = BlockBuilder(parser)

    @Test
    fun `test empty block`() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(PunctuationType, "}", 1, 2),
            )

        val result = blockBuilder.buildNode(tokens)

        assertTrue(result is BlockNode)
        assertEquals(0, (result as BlockNode).statements.size)
    }

    @Test
    fun `test block with single statement`() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 2),
                Token(ModifierType, "let", 1, 3),
                Token(IdentifierType, "x", 1, 4),
                Token(AssignmentType, "=", 1, 5),
                Token(LiteralNumber, "42", 1, 6),
                Token(PunctuationType, ";", 1, 7),
                Token(PunctuationType, "}", 1, 8),
            )

        val result = blockBuilder.buildNode(tokens)

        assertTrue(result is BlockNode)
        assertEquals(1, (result as BlockNode).statements.size)
    }

    @Test
    fun `test block with multiple statements`() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(ModifierType, "let", 1, 2),
                Token(IdentifierType, "x", 1, 3),
                Token(AssignmentType, "=", 1, 4),
                Token(LiteralNumber, "42", 1, 5),
                Token(PunctuationType, ";", 1, 6),
                Token(ModifierType, "let", 1, 7),
                Token(IdentifierType, "y", 1, 8),
                Token(AssignmentType, "=", 1, 9),
                Token(LiteralNumber, "10", 1, 10),
                Token(PunctuationType, ";", 1, 11),
                Token(PunctuationType, "}", 1, 12),
            )

        val result = blockBuilder.buildNode(tokens)

        assertTrue(result is BlockNode)
        assertEquals(2, (result as BlockNode).statements.size)
    }

   /* @Test
    fun `test block with const statement`() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(ModifierType, "const", 1, 2),
                Token(IdentifierType, "PI", 1, 3),
                Token(AssignmentType, "=", 1, 4),
                Token(LiteralNumber, "3.14", 1, 5),
                Token(PunctuationType, ";", 1, 6),
                Token(PunctuationType, "}", 1, 7),
            )

        val result = blockBuilder.buildNode(tokens)

        assertTrue(result is BlockNode)
        assertEquals(1, (result as BlockNode).statements.size)
    }*/
}
