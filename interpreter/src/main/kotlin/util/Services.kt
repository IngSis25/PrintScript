package org.example.util

import org.example.astnode.ASTNode
import org.example.input.Input
import org.example.output.Output

/**
 * Mochila de ejecución que reciben TODAS las strategies.
 * - context: estado (inmutable) de variables
 * - output: destino de impresión
 * - input: fuente de entrada
 * - visit: cómo evaluar sub-nodos (la inyecta el Interpreter)
 */
data class Services(
    val context: Map<String, Any?>,
    val output: Output,
    val input: Input,
    val visit: (Services, ASTNode) -> Any?,
)
