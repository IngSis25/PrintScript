package test.lexerTest

import main.kotlin.lexer.*
import org.testng.Assert.assertEquals
import org.testng.annotations.Test


class TokenTypesTest {

    @Test
    fun tokenTypeShouldBeLiteralNumber() {
        val token = Token(
            type = LiteralNumber,
            value = "5",
            line = 1,
            column = 1
        )

        assertEquals(LiteralNumber, token.type)
    }

    @Test
    fun tokenTypeShouldBeLiteralString() {
        val token = Token(
            type = LiteralString,
            value = "Olivia",
            line = 1,
            column = 1
        )
        assertEquals(LiteralString, token.type)
    }

    @Test
    fun tokenTypeShouldBeIdentifierType() {
        val token = Token(
            type = IdentifierType,
            value = "name",
            line = 1,
            column = 1
        )
        assertEquals(IdentifierType, token.type)
    }

    @Test
    fun tokenTypeShouldBeOperatorType() {
        val token = Token(
            type = OperatorType,
            value = "+",
            line = 1,
            column = 1
        )
        assertEquals(OperatorType, token.type)
    }

    @Test
    fun tokenTypeShouldBeNumberType() {
        val token = Token(
            type = NumberType,
            value = "Int",
            line = 1,
            column = 1
        )
        assertEquals(NumberType, token.type)
    }

    @Test
    fun tokenTypeShouldBePunctuationType() {
        val token = Token(
            type = PunctuationType,
            value = ";",
            line = 1,
            column = 1
        )
        assertEquals(PunctuationType, token.type)
    }

    @Test
    fun tokenTypeShouldBeStringType() {
        val token = Token(
            type = StringType,
            value = "String",
            line = 1,
            column = 1
        )
        assertEquals(StringType, token.type)
    }
}


