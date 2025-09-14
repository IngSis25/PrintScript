package org.example.strategy

import org.example.ast.BinaryOpNode
import org.example.util.Services

// Ajustá el import del nodo a tu parser real
// import main.kotlin.parser.BinaryOpNode

val binaryExpressionStrategy =
    Strategy<BinaryOpNode> { services: Services, node: BinaryOpNode ->
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
