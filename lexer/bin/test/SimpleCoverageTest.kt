package main.kotlin.lexer

import factory.LexerFactoryRegistry
import factory.LexerFactoryV1
import factory.LexerFactoryV11
import org.example.LiteralNumber
import org.example.LiteralString
import org.junit.jupiter.api.Test
import types.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimpleCoverageTest {
    @Test
    fun testAllTokenTypes() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "let x = 42; string name = \"hello\"; println(\"test\");"
        val tokens = lexer.tokenize(code)

        // Verificar diferentes tipos de tokens
        val letToken = tokens.find { it.value == "let" }
        assertTrue(letToken != null)
        assertTrue(letToken!!.type is ModifierType)

        val numberToken = tokens.find { it.value == "42" }
        assertTrue(numberToken != null)
        assertTrue(numberToken!!.type is LiteralNumber)

        val stringToken = tokens.find { it.value == "\"hello\"" }
        assertTrue(stringToken != null)
        assertTrue(stringToken!!.type is LiteralString)

        val printlnToken = tokens.find { it.value == "println" }
        assertTrue(printlnToken != null)
        assertTrue(printlnToken!!.type is PrintlnType)
    }

    @Test
    fun testTokenProviderFromMap() {
        val tokenMap =
            mapOf(
                "test" to PrintlnType,
                "123" to LiteralNumber,
            )

        val provider = TokenProvider.fromMap(tokenMap)
        val rules = provider.rules()

        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun testTokenCreation() {
        val token = Token(PrintlnType, "println", 1, 1)

        assertEquals(PrintlnType, token.type)
        assertEquals("println", token.value)
        assertEquals(1, token.line)
        assertEquals(1, token.column)
    }

    @Test
    fun testTokenRuleCreation() {
        val rule = TokenRule(Regex("\\d+"), LiteralNumber, ignore = false)

        assertEquals(LiteralNumber, rule.type)
        assertEquals(false, rule.ignore)
    }

    @Test
    fun testLexicalException() {
        val exception = LexicalException("Test error")
        assertEquals("Test error", exception.message)
    }

    @Test
    fun testAllTypeObjects() {
        // Verificar que todos los tipos tienen el nombre correcto
        assertEquals("PRINTLN", PrintlnType.name)
        assertEquals("NUMBER_KEYWORD", NumberType.name)
        assertEquals("STRING_KEYWORD", StringType.name)
        assertEquals("IDENTIFIER", IdentifierType.name)
        assertEquals("PUNCTUATION", PunctuationType.name)
        assertEquals("OPERATOR", OperatorType.name)
        assertEquals("ASSIGNMENT", AssignmentType.name)
        assertEquals("MODIFIER", ModifierType.name)
    }

    @Test
    fun testComplexExpression() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "let result = (x + y) * 2;"
        val tokens = lexer.tokenize(code)

        // Verificar que se procesan correctamente los paréntesis y operadores
        val openParen = tokens.find { it.value == "(" }
        assertTrue(openParen != null)
        assertTrue(openParen!!.type is PunctuationType)

        val closeParen = tokens.find { it.value == ")" }
        assertTrue(closeParen != null)
        assertTrue(closeParen!!.type is PunctuationType)

        val plusToken = tokens.find { it.value == "+" }
        assertTrue(plusToken != null)
        assertTrue(plusToken!!.type is OperatorType)

        val multiplyToken = tokens.find { it.value == "*" }
        assertTrue(multiplyToken != null)
        assertTrue(multiplyToken!!.type is OperatorType)
    }

    @Test
    fun testStringWithEscapes() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "let message = \"Hello \\\"world\\\"\";"
        val tokens = lexer.tokenize(code)

        val stringToken = tokens.find { it.value.contains("Hello") }
        assertTrue(stringToken != null)
        assertTrue(stringToken!!.type is LiteralString)
    }

    @Test
    fun testDecimalNumbers() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "let pi = 3.14159;"
        val tokens = lexer.tokenize(code)

        val decimalToken = tokens.find { it.value == "3.14159" }
        assertTrue(decimalToken != null)
        assertTrue(decimalToken!!.type is LiteralNumber)
    }

    @Test
    fun testComparisonOperators() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "x + y;"
        val tokens = lexer.tokenize(code)

        val plusToken = tokens.find { it.value == "+" }
        assertTrue(plusToken != null)
        assertTrue(plusToken!!.type is OperatorType)
    }

    @Test
    fun testFactoryRegistry() {
        // Test V1.0 factory
        val factoryV1 = LexerFactoryRegistry.getFactory("1.0")
        assertTrue(factoryV1 is LexerFactoryV1)

        // Test V1.1 factory
        val factoryV11 = LexerFactoryRegistry.getFactory("1.1")
        assertTrue(factoryV11 is LexerFactoryV11)

        // Test invalid version
        try {
            LexerFactoryRegistry.getFactory("2.0")
            assertTrue(false, "Should have thrown exception")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Versión no soportada"))
        }
    }
}
