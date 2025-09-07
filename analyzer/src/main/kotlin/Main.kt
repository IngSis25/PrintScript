package main.kotlin.analyzer

import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import java.io.File

fun main() {
    println("=== ANALIZADOR ESTÁTICO CON GSON ===\n")

    // 1. Crear archivo de configuración JSON
    val configFile = File("analyzer-config.json")
    createConfigFile(configFile)

    // 2. Cargar configuración desde JSON
    val config = ConfigLoader.loadFromJson(configFile)

    println("Configuración cargada desde JSON:")
    println("  - Formato de identificadores: ${config.identifierFormat.format}")
    println("  - Restricciones println: ${config.printlnRestrictions.enabled}")
    println("  - Máximo errores: ${config.maxErrors}")
    println("  - Modo estricto: ${config.strictMode}")
    println()

    val program = createTestProgram()

    println("Analizando programa...")
    println("  - Variables declaradas: 4")
    println("  - Llamadas a println: 4")
    println()

    val analyzer = DefaultAnalyzer()
    val result = analyzer.analyze(program, config)

    printAnalysisResults(result)

    testWithDifferentConfig()
}

fun createConfigFile(file: File) {
    val jsonConfig =
        """
        {
          "identifierFormat": {
            "enabled": true,
            "format": "CAMEL_CASE"
          },
          "printlnRestrictions": {
            "enabled": true,
            "allowOnlyIdentifiersAndLiterals": true
          },
          "maxErrors": 10,
          "enableWarnings": true,
          "strictMode": false
        }
        """.trimIndent()

    file.writeText(jsonConfig)
    println("📄 Archivo de configuración creado: ${file.name}")
}

fun createTestProgram(): List<ASTNode> =
    listOf(
        VariableDeclarationNode(
            identifier = IdentifierNode("myVariable"),
            varType = "NUMBER",
            value = LiteralNode("42", LiteralNumber),
        ),
        VariableDeclarationNode(
            identifier = IdentifierNode("bad_name"),
            varType = "STRING",
            value = LiteralNode("hello", LiteralString),
        ),
        VariableDeclarationNode(
            identifier = IdentifierNode("BadName"),
            varType = "NUMBER",
            value = LiteralNode("100", LiteralNumber),
        ),
        VariableDeclarationNode(
            identifier = IdentifierNode("2badName"),
            varType = "STRING",
            value = LiteralNode("test", LiteralString),
        ),
        PrintlnNode(
            value = IdentifierNode("myVariable"),
        ),
        PrintlnNode(
            value = LiteralNode("Hello World", LiteralString),
        ),
        PrintlnNode(
            value =
                BinaryOpNode(
                    left = IdentifierNode("myVariable"),
                    operator = "+",
                    right = LiteralNode("1", LiteralNumber),
                ),
        ),
        PrintlnNode(
            value =
                BinaryOpNode(
                    left = LiteralNode("5", LiteralNumber),
                    operator = "*",
                    right = LiteralNode("3", LiteralNumber),
                ),
        ),
    )

fun printAnalysisResults(result: AnalysisResult) {
    println("�� RESULTADOS DEL ANÁLISIS:")
    println("  - Errores encontrados: ${result.errorCount}")
    println("  - Advertencias: ${result.warningCount}")
    println("  - Análisis exitoso: ${if (result.success) "✅ SÍ" else "❌ NO"}")
    println()

    if (result.diagnostics.isNotEmpty()) {
        println("�� DIAGNÓSTICOS ENCONTRADOS:")
        println("=" * 50)

        result.diagnostics.forEachIndexed { index, diagnostic ->
            val emoji =
                when (diagnostic.severity) {
                    DiagnosticSeverity.ERROR -> "❌"
                    DiagnosticSeverity.WARNING -> "⚠️"
                    DiagnosticSeverity.INFO -> "ℹ️"
                }

            println("${index + 1}. $emoji ${diagnostic.code}")
            println("   📝 Mensaje: ${diagnostic.message}")
            println("   📍 Posición: ${diagnostic.position}")

            if (diagnostic.suggestions.isNotEmpty()) {
                println("  Sugerencias:")
                diagnostic.suggestions.forEach { suggestion ->
                    println("      • $suggestion")
                }
            }
            println()
        }
    } else {
        println("¡No se encontraron problemas!")
    }

    // Mostrar resumen
    println("RESUMEN:")
    if (result.success) {
        println("   ¡Código limpio! No hay errores.")
    } else {
        println("   Se encontraron ${result.errorCount} errores que corregir.")
        println("   Revisa los diagnósticos arriba para solucionarlos.")
    }
    println()
}

fun testWithDifferentConfig() {
    println("🔄 PROBANDO CON CONFIGURACIÓN DIFERENTE...")

    // Crear configuración permisiva
    val permissiveConfig =
        """
        {
          "identifierFormat": {
            "enabled": false
          },
          "printlnRestrictions": {
            "enabled": false
          },
          "maxErrors": 100,
          "enableWarnings": false,
          "strictMode": false
        }
        """.trimIndent()

    val config = ConfigLoader.loadFromJsonString(permissiveConfig)
    val program = createTestProgram()
    val analyzer = DefaultAnalyzer()
    val result = analyzer.analyze(program, config)

    println(" Configuración permisiva:")
    println("  - Formato de identificadores: ${if (config.identifierFormat.enabled) "HABILITADO" else "DESHABILITADO"}")
    println("  - Restricciones println: ${if (config.printlnRestrictions.enabled) "HABILITADO" else "DESHABILITADO"}")
    println("  - Errores encontrados: ${result.errorCount}")
    println("  - Análisis exitoso: ${if (result.success) " SÍ" else " NO"}")
    println()

    if (result.errorCount == 0) {
        println("¡Perfecto! Con configuración permisiva no hay errores.")
    } else {
        println(" Aún hay errores con configuración permisiva...")
    }
}

// Función helper para repetir strings
private operator fun String.times(n: Int): String = this.repeat(n)
