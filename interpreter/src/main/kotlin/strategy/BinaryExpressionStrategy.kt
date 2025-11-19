package org.example.strategy

import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.util.Services

val binaryExpressionStrategy =
    Strategy<BinaryExpressionNode> { services: Services, node: BinaryExpressionNode ->
        val leftVal = services.visit(services, node.left)
        val rightVal = services.visit(services, node.right)

        when (node.operator) {
            "+" -> {
                // Concatenación de strings o suma de números
                when {
                    leftVal is String && rightVal is String -> leftVal + rightVal
                    leftVal is String && rightVal is Double -> leftVal + formatNumber(rightVal)
                    leftVal is Double && rightVal is String -> formatNumber(leftVal) + rightVal
                    leftVal is Double && rightVal is Double -> leftVal + rightVal
                    else -> throw RuntimeException(
                        "Operación + no soportada entre ${leftVal!!::class.simpleName} y ${rightVal!!::class.simpleName}",
                    )
                }
            }

            "-", "*", "/" -> {
                // Solo operaciones numéricas
                val leftNum = leftVal as? Double ?: throw RuntimeException("Operando izquierdo no es número: $leftVal")
                val rightNum = rightVal as? Double ?: throw RuntimeException("Operando derecho no es número: $rightVal")

                when (node.operator) {
                    "-" -> leftNum - rightNum
                    "*" -> leftNum * rightNum
                    "/" -> {
                        if (rightNum == 0.0) throw ArithmeticException("División por cero")
                        leftNum / rightNum
                    }

                    else -> throw RuntimeException("Operador no soportado: ${node.operator}")
                }
            }

            "==", "!=", ">", "<", ">=", "<=" -> {
                // Operadores de comparación - devuelven Boolean
                when {
                    // Comparación de números
                    leftVal is Double && rightVal is Double -> {
                        when (node.operator) {
                            "==" -> leftVal == rightVal
                            "!=" -> leftVal != rightVal
                            ">" -> leftVal > rightVal
                            "<" -> leftVal < rightVal
                            ">=" -> leftVal >= rightVal
                            "<=" -> leftVal <= rightVal
                            else -> throw RuntimeException("Operador de comparación no soportado: ${node.operator}")
                        }
                    }
                    // Comparación de strings
                    leftVal is String && rightVal is String -> {
                        when (node.operator) {
                            "==" -> leftVal == rightVal
                            "!=" -> leftVal != rightVal
                            else -> throw RuntimeException(
                                "Operador de comparación no soportado para strings: ${node.operator}",
                            )
                        }
                    }
                    // Comparación de booleans
                    leftVal is Boolean && rightVal is Boolean -> {
                        when (node.operator) {
                            "==" -> leftVal == rightVal
                            "!=" -> leftVal != rightVal
                            else -> throw RuntimeException(
                                "Operador de comparación no soportado para booleans: ${node.operator}",
                            )
                        }
                    }
                    else -> throw RuntimeException(
                        "Comparación no soportada entre ${leftVal!!::class.simpleName} y ${rightVal!!::class.simpleName}",
                    )
                }
            }

            else -> throw RuntimeException("Operador no soportado: ${node.operator}")
        }
    }

private fun formatNumber(value: Double): String {
    // Si es un número entero, mostrarlo sin decimales
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        value.toString()
    }
}
