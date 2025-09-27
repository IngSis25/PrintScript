package org.example.formatter

import com.google.gson.Gson
import org.example.ast.ASTNode
import org.example.formatter.config.FormatterConfig
import java.io.File

// Punto de entrada único que combina JSON/Config → salida formateada
// - format(...) y formatMultiple(...) siguen usando el Visitor (AST → String).
// - NUEVO: formatSource(...) permite el "modo minimalista": solo cortar por ';' preservando lo demás.

data object Formatter {
    /**
     * NUEVO: Formatea directamente desde el source crudo.
     * - Si onlyLineBreakAfterStatement = true → SOLO inserta '\n' tras cada ';' (fuera de strings).
     * - Si es false → (opcional) podrías parsear y usar el Visitor; acá devolvemos tal cual.
     */
    fun formatSource(
        source: String,
        json: File,
    ): String {
        val config = Gson().fromJson(json.readText(Charsets.UTF_8), FormatterConfig::class.java)

        return if (config.onlyLineBreakAfterStatement) {
            val s = breakOnlyAfterSemicolons(source)
            if (s.isNotEmpty() && s.last() == '\n') s.dropLast(1) else s
        } else {
            source
        }
    }

    fun format(
        node: ASTNode,
        json: File,
    ): String {
        // 1. Leer y parsear JSON con Gson
        val config = Gson().fromJson(json.readText(Charsets.UTF_8), FormatterConfig::class.java)

        // 2. Crear visitor y procesar (ruta AST → String)
        val result = StringBuilder()
        val visitor = FormatterVisitor(config, result)
        visitor.evaluate(node)

        return result.toString()
    }

    fun formatMultiple(
        nodes: List<ASTNode>,
        json: File,
    ): String {
        // 1. Leer y parsear JSON con Gson
        val config = Gson().fromJson(json.readText(Charsets.UTF_8), FormatterConfig::class.java)

        // 2. Crear visitor y procesar múltiples nodos (ruta AST → String)
        val result = StringBuilder()
        val visitor = FormatterVisitor(config, result)
        visitor.evaluateMultiple(nodes)

        return result.toString()
    }

    // =========================
    // Helper textual (privado)
    // =========================

    /**
     * Inserta un '\n' después de cada ';' que no esté dentro de un string.
     * - No modifica nada más (espacios, ':', '=', etc. quedan tal cual).
     * - Si ya hay un '\n' inmediatamente después del ';' (ignorando espacios/tabs), no agrega otro.
     */
    private fun breakOnlyAfterSemicolons(source: String): String {
        val out = StringBuilder(source.length + 16)
        var i = 0
        var inString = false
        var escape = false

        while (i < source.length) {
            val c = source[i]
            out.append(c)

            if (inString) {
                if (escape) {
                    escape = false
                } else if (c == '\\') {
                    escape = true
                } else if (c == '"') {
                    inString = false
                }
                i++
                continue
            }

            if (c == '"') {
                inString = true
                i++
                continue
            }

            if (c == ';') {
                var j = i + 1
                // Saltar espacios/tabs posteriores (los preservamos, no los borramos)
                while (j < source.length && (source[j] == ' ' || source[j] == '\t')) j++
                // Si no hay salto de línea, lo agregamos
                if (!(j < source.length && source[j] == '\n')) out.append('\n')
                i = j
                continue
            }

            i++
        }
        return out.toString()
    }
}
