package test.parserTest

import builders.ReadEnvBuilder
import main.kotlin.lexer.*
import ast.ReadEnvNode
import types.*
import kotlin.test.*

class ReadEnvBuilderTests {
    private val readEnvBuilder = ReadEnvBuilder()

    @Test
    fun should_build_readenv_node_with_string_argument() {
        val tokens = listOf(
            Token(ReadEnvType, "readEnv", 1, 1),
            Token(PunctuationType, "(", 1, 8),
            Token(StringType, "\"BEST_FOOTBALL_CLUB\"", 1, 9),
            Token(PunctuationType, ")", 1, 30),
        )

        val result = readEnvBuilder.buildNode(tokens)

        assertTrue(result is ReadEnvNode)
        val readEnvNode = result as ReadEnvNode
        assertEquals("BEST_FOOTBALL_CLUB", readEnvNode.envVarName)
    }

    @Test
    fun should_build_readenv_node_with_different_env_var() {
        val tokens = listOf(
            Token(ReadEnvType, "readEnv", 1, 1),
            Token(PunctuationType, "(", 1, 8),
            Token(StringType, "\"DATABASE_URL\"", 1, 9),
            Token(PunctuationType, ")", 1, 22),
        )

        val result = readEnvBuilder.buildNode(tokens)

        assertTrue(result is ReadEnvNode)
        val readEnvNode = result as ReadEnvNode
        assertEquals("DATABASE_URL", readEnvNode.envVarName)
    }

    @Test
    fun should_throw_exception_for_wrong_number_of_tokens() {
        val tokens = listOf(
            Token(ReadEnvType, "readEnv", 1, 1),
            Token(PunctuationType, "(", 1, 8),
            // Missing string argument and closing parenthesis
        )

        assertFailsWith<IllegalArgumentException> {
            readEnvBuilder.buildNode(tokens)
        }
    }

    @Test
    fun should_throw_exception_for_non_string_argument() {
        val tokens = listOf(
            Token(ReadEnvType, "readEnv", 1, 1),
            Token(PunctuationType, "(", 1, 8),
            Token(LiteralNumber, "123", 1, 9), // Number instead of string
            Token(PunctuationType, ")", 1, 12),
        )

        assertFailsWith<AssertionError> {
            readEnvBuilder.buildNode(tokens)
        }
    }

    @Test
    fun should_throw_exception_for_missing_opening_parenthesis() {
        val tokens = listOf(
            Token(ReadEnvType, "readEnv", 1, 1),
            Token(StringType, "\"BEST_FOOTBALL_CLUB\"", 1, 8),
            Token(PunctuationType, ")", 1, 29),
        )

        assertFailsWith<AssertionError> {
            readEnvBuilder.buildNode(tokens)
        }
    }

    @Test
    fun should_throw_exception_for_missing_closing_parenthesis() {
        val tokens = listOf(
            Token(ReadEnvType, "readEnv", 1, 1),
            Token(PunctuationType, "(", 1, 8),
            Token(StringType, "\"BEST_FOOTBALL_CLUB\"", 1, 9),
        )

        assertFailsWith<AssertionError> {
            readEnvBuilder.buildNode(tokens)
        }
    }
}