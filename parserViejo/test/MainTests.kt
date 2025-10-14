package test.parserTest

import main.kotlin.lexer.*
import main.kotlin.parser.ConfiguredRules
import main.kotlin.parser.DefaultParser
import org.example.ast.*
import rules.RuleMatcher
import types.*
import kotlin.test.*

class MainTests {
    @Test
    fun main_function_should_parse_simple_code() {
        // This test verifies that the main function can be called without errors
        // We can't easily test the main function directly, but we can test its components

        val baseProvider = ConfiguredTokens.providerV1()

        val ignored =
            listOf(
                TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
            )

        val tokenProvider = TokenProvider(ignored + baseProvider.rules())
        val lexer = DefaultLexer(tokenProvider)

        val code =
            """
            let nombre: string = "Achu";
            println("hola");
            println(12 + 8);
            """.trimIndent()

        // Test lexer
        val tokens = lexer.tokenize(code)
        assertTrue(tokens.isNotEmpty())

        // Test parser
        val ruleMatcher = RuleMatcher(ConfiguredRules.V1)
        val parser = DefaultParser(ruleMatcher)
        val ast = parser.parse(tokens)

        assertTrue(ast.isNotEmpty())
        assertTrue(ast.any { it is VariableDeclarationNode })
        assertTrue(ast.any { it is PrintlnNode })
    }
}
