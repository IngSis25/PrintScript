package org.example.strategy

import org.example.ast.BinaryOpNode
import org.example.util.Services

// Ajustá el import del nodo a tu parser real
// import main.kotlin.parser.BinaryOpNode

val binaryExpressionStrategy =
    Strategy<BinaryOpNode> { services: Services, node: BinaryOpNode ->
        val leftVal = services.visit(services, node.left)
        val rightVal = services.visit(services, node.right)

        val leftNum = leftVal as? Double ?: throw RuntimeException("Operando izquierdo no es número: $leftVal")
        val rightNum = rightVal as? Double ?: throw RuntimeException("Operando derecho no es número: $rightVal")

        when (node.operator) {
            "+" -> leftNum + rightNum
            "-" -> leftNum - rightNum
            "*" -> leftNum * rightNum
            "/" -> {
                if (rightNum == 0.0) throw ArithmeticException("División por cero")
                leftNum / rightNum
            }
            else -> throw RuntimeException("Operador no soportado: ${node.operator}")
        }
    }
