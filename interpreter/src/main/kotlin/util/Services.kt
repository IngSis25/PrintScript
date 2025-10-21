package org.example.util

import org.example.ast.ASTNode
import org.example.output.Output

/**
 * Mochila de ejecución que reciben TODAS las strategies.
 * - context: estado (inmutable) de variables
 * - output: destino de impresión
 * - visit: cómo evaluar sub-nodos (la inyecta el Interpreter)
 */
data class Services(
    val context: Map<String, Any?>,
    val output: Output,
    val visit: (Services, ASTNode) -> Any?,
    val environment: Map<String, String> = emptyMap(),
)
