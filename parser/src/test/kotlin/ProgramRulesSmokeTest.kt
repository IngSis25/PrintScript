package test.parserTest

import builders.AssignmentBuilder
import builders.ConstBuilder
import builders.IfNodeBuilder
import builders.PrintBuilder
import factory.LexerFactoryV11
import main.kotlin.lexer.Token
import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import org.example.DefaultInterpreter
import org.example.LiteralString
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import org.junit.jupiter.api.Test
import rules.AssignmentRule
import rules.ConstRule
import rules.IfRule
import rules.PrintlnRule
import rules.RuleMatcher
import types.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProgramRulesSmokeTest {
    // Include IfRule; builder isn't used by matchNext so a simple parser is fine
    fun productionRules() =
        listOf(
            ConstRule(ConstBuilder()),
            IfRule(IfNodeBuilder(DefaultParser(RuleMatcher(emptyList())))),
            AssignmentRule(AssignmentBuilder()),
            PrintlnRule(PrintBuilder()),
        )

    private fun buildTokens(): List<Token> =
        listOf(
            // const booleanValue: Boolean = false;
            Token(ModifierType, "const", 1, 1),
            Token(IdentifierType, "booleanValue", 1, 7),
            Token(PunctuationType, ":", 1, 20),
            Token(BooleanType, "Boolean", 1, 22), // type token (BooleanType)
            Token(AssignmentType, "=", 1, 30),
            Token(LiteralBoolean, "false", 1, 32), // literal boolean
            Token(PunctuationType, ";", 1, 37),
            // if (booleanValue) { println("if statement is not working correctly"); }
            Token(IfType, "if", 2, 1),
            Token(PunctuationType, "(", 2, 3),
            Token(IdentifierType, "booleanValue", 2, 4),
            Token(PunctuationType, ")", 2, 16),
            Token(PunctuationType, "{", 2, 18),
            Token(PrintlnType, "println", 3, 5),
            Token(PunctuationType, "(", 3, 12),
            Token(LiteralString, "\"if statement is not working correctly\"", 3, 13),
            Token(PunctuationType, ")", 3, 54),
            Token(PunctuationType, ";", 3, 55),
            Token(PunctuationType, "}", 4, 1),
            // println("outside of conditional");
            Token(PrintlnType, "println", 5, 1),
            Token(PunctuationType, "(", 5, 8),
            Token(LiteralString, "\"outside of conditional\"", 5, 9),
            Token(PunctuationType, ")", 5, 33),
            Token(PunctuationType, ";", 5, 34),
        )

    private fun splitTopLevelStatements(tokens: List<Token>): List<List<Token>> {
        val parts = mutableListOf<MutableList<Token>>()
        var current = mutableListOf<Token>()
        var paren = 0
        var brace = 0

        fun flush() {
            if (current.isNotEmpty()) {
                parts += current
                current = mutableListOf()
            }
        }

        tokens.forEach { t ->
            current += t
            when (t.value) {
                "(" -> paren++
                ")" -> paren--
                "{" -> brace++
                "}" -> {
                    brace--
                    if (brace == 0 && paren == 0) flush()
                }
                ";" -> if (paren == 0 && brace == 0) flush()
            }
        }
        if (current.isNotEmpty()) parts += current
        return parts
    }

    @Test
    fun should_match_const_if_block_and_println_in_order() {
        val rules = productionRules()
        val matcher = RuleMatcher(rules)

        val tokens = buildTokens()
        val statements = splitTopLevelStatements(tokens)

        assertEquals(3, statements.size, "Se esperaban 3 statements top-level")

        val r1 = matcher.matchNext(statements[0], 0)
        assertNotNull(r1, "No matcheó la declaración const")
        assertTrue(r1 is main.kotlin.parser.ParseResult.Success, "const no devolvió Success")

        val r2 = matcher.matchNext(statements[1], 0)
        assertNotNull(r2, "No matcheó el if-block")
        assertTrue(r2 is main.kotlin.parser.ParseResult.Success, "if-block no devolvió Success")

        val r3 = matcher.matchNext(statements[2], 0)
        assertNotNull(r3, "No matcheó el println final")
        assertTrue(r3 is main.kotlin.parser.ParseResult.Success, "println final no devolvió Success")
    }

    @Test
    fun should_find_rule() {
        val code =
            "const booleanResult: Boolean = true;\n" +
                "if(booleanResult) {\n" +
                "    println(\"else statement working correctly\");\n" +
                "} else {\n" +
                "    println(\"else statement not working correctly\");\n" +
                "}\n" +
                "println(\"outside of conditional\");\n"
        val lexer = LexerFactoryV11().create()
        val baseParser = DefaultParser(RuleMatcher(ConfiguredRules.V1))
        val v11Rules = ConfiguredRules.createV11Rules(baseParser)
        val v11RuleMatcher = RuleMatcher(v11Rules)
        val matcher = RuleMatcher(v11Rules)
        val parser = DefaultParser(matcher)
        val output =
            object : Output {
                override fun write(msg: String) {
                    println(msg)
                }
            }
        val provider = PreConfiguredProviders.VERSION_1_1
        val interpreter = DefaultInterpreter(output, provider)
        val tokenized = lexer.tokenize(code)
        val parsed = parser.parse(tokenized)
        parsed.forEach {
            interpreter.interpret(it)
        }
    }
}
