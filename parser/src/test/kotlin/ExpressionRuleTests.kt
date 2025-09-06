package test.parserTest

import builders.ExpressionBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import kotlin.test.*
import rules.ExpressionRule
import types.OperatorType

class ExpressionRuleTests {
    
    @Test
    fun expressionRule_should_match_simple_addition_pattern() {
        // Arrange
        val rule = ExpressionRule(ExpressionBuilder())
        val tokens = listOf(
            Token(LiteralNumber, "12", 1, 1),
            Token(OperatorType, "+", 1, 4),
            Token(LiteralNumber, "8", 1, 6)
        )
        
        // Act
        val result = rule.matcher.match(tokens, 0)
        
        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        
        val success = result as ParseResult.Success
        assertEquals(3, success.nextPosition) // consumió los 3 tokens
    }
    
    @Test
    fun expressionRule_should_match_subtraction_pattern() {
        // Arrange
        val rule = ExpressionRule(ExpressionBuilder())
        val tokens = listOf(
            Token(LiteralNumber, "20", 1, 1),
            Token(OperatorType, "-", 1, 4),
            Token(LiteralNumber, "5", 1, 6)
        )
        
        // Act
        val result = rule.matcher.match(tokens, 0)
        
        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        
        val success = result as ParseResult.Success
        assertEquals(3, success.nextPosition)
    }
    
    @Test
    fun expressionRule_should_not_match_incomplete_expression() {
        // Arrange - solo 2 tokens, falta el operando derecho
        val rule = ExpressionRule(ExpressionBuilder())
        val tokens = listOf(
            Token(LiteralNumber, "12", 1, 1),
            Token(OperatorType, "+", 1, 4)
            // Falta el segundo número
        )
        
        // Act
        val result = rule.matcher.match(tokens, 0)
        
        // Assert
        assertNull(result) // no debería matchear
    }
    
    @Test
    fun expressionRule_should_not_match_wrong_token_order() {
        // Arrange - orden incorrecto: operador primero
        val rule = ExpressionRule(ExpressionBuilder())
        val tokens = listOf(
            Token(OperatorType, "+", 1, 1),
            Token(LiteralNumber, "12", 1, 3),
            Token(LiteralNumber, "8", 1, 6)
        )
        
        // Act
        val result = rule.matcher.match(tokens, 0)
        
        // Assert
        assertNull(result) // no debería matchear
    }
}
