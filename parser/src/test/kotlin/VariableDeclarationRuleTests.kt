package test.parserTest

import builders.VariableDeclarationBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import org.example.LiteralString
import rules.VariableDeclarationRule
import types.AssignmentType
import types.IdentifierType
import types.ModifierType
import types.NumberType
import types.PunctuationType
import types.StringType
import kotlin.test.*

class VariableDeclarationRuleTests {
    @Test
    fun variableDeclarationRule_should_match_let_declaration_pattern() {
        // Arrange
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "nombre", 1, 5),
                Token(AssignmentType, "=", 1, 12),
                Token(LiteralString, "\"Juan\"", 1, 14),
                Token(PunctuationType, ";", 1, 20),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(5, success.nextPosition) // consumió todos los tokens
    }

    @Test
    fun variableDeclarationRule_should_match_var_declaration_pattern() {
        // Arrange - con 'var' en lugar de 'let'
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "var", 1, 1),
                Token(IdentifierType, "edad", 1, 5),
                Token(AssignmentType, "=", 1, 10),
                Token(LiteralString, "\"25\"", 1, 12),
                Token(PunctuationType, ";", 1, 16),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(5, success.nextPosition)
    }

    @Test
    fun variableDeclarationRule_should_not_match_without_semicolon() {
        // Arrange - falta el punto y coma
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "nombre", 1, 5),
                Token(AssignmentType, "=", 1, 12),
                Token(LiteralString, "\"Juan\"", 1, 14),
                // Falta el ;
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun variableDeclarationRule_should_not_match_without_assignment() {
        // Arrange - falta el operador de asignación
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "nombre", 1, 5),
                Token(LiteralString, "\"Juan\"", 1, 7),
                Token(PunctuationType, ";", 1, 13),
                // Falta el =
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun variableDeclarationRule_should_not_match_wrong_modifier() {
        // Arrange - empieza con palabra incorrecta
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(IdentifierType, "const", 1, 1), // no es ModifierType
                Token(IdentifierType, "nombre", 1, 7),
                Token(AssignmentType, "=", 1, 14),
                Token(LiteralString, "\"Juan\"", 1, 16),
                Token(PunctuationType, ";", 1, 22),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun variableDeclarationRule_should_match_declaration_without_initialization_number() {
        // Arrange - let pi: number;
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "pi", 1, 5),
                Token(PunctuationType, ":", 1, 7),
                Token(NumberType, "number", 1, 9),
                Token(PunctuationType, ";", 1, 15),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(5, (success.node as List<*>).size) // let, pi, :, number, ;
        assertEquals(5, success.nextPosition)
    }

    @Test
    fun variableDeclarationRule_should_match_declaration_without_initialization_string() {
        // Arrange - let name: string;
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "name", 1, 5),
                Token(PunctuationType, ":", 1, 9),
                Token(StringType, "string", 1, 11),
                Token(PunctuationType, ";", 1, 17),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(5, (success.node as List<*>).size) // let, name, :, string, ;
        assertEquals(5, success.nextPosition)
    }

    @Test
    fun variableDeclarationRule_should_not_match_declaration_without_initialization_wrong_type() {
        // Arrange - let x: invalid;
        val rule = VariableDeclarationRule(VariableDeclarationBuilder())
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "x", 1, 5),
                Token(PunctuationType, ":", 1, 6),
                Token(IdentifierType, "invalid", 1, 8), // no es NumberType ni StringType
                Token(PunctuationType, ";", 1, 15),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }
}
