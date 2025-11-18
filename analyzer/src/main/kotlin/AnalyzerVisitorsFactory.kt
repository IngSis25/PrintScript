package main.kotlin.analyzer

import com.google.gson.JsonObject
import org.checkvisitors.NamingFormatVisitor
import org.checkvisitors.PrintUseVisitor
import org.checkvisitors.ReadInputVisitor
import org.checkvisitors.UnusedVariableVisitor
import org.example.astnode.PatternFactory
import visitors.AnalyzerVisitor

class AnalyzerVisitorsFactory {
    fun createAnalyzerDefaultVisitors(version: String): List<AnalyzerVisitor> =
        when (version) {
            "1.0" -> createAvailableAnalyzerVisitorsV10()
            "1.1" -> createAvailableAnalyzerVisitorsV11()
            else -> throw IllegalArgumentException("Unsupported analyzer version: $version")
        }

    fun createAnalyzerVisitorsFromJson(
        version: String,
        jsonConfig: JsonObject?,
    ): List<AnalyzerVisitor> =
        if (jsonConfig == null) {
            createAnalyzerDefaultVisitors(version)
        } else {
            when (version) {
                "1.0" -> createAnalyzerVisitorsV10FromJson(jsonConfig)
                "1.1" -> createAnalyzerVisitorsV11FromJson(jsonConfig)
                else -> throw IllegalArgumentException("Unsupported analyzer version: $version")
            }
        }

    private fun createAvailableAnalyzerVisitorsV10(): List<AnalyzerVisitor> {
        val visitors = mutableListOf<AnalyzerVisitor>()
        visitors.add(UnusedVariableVisitor())
        visitors.add(NamingFormatVisitor("camelCase", PatternFactory.getNamingFormatPattern("camelCase")))
        return visitors
    }

    private fun createAvailableAnalyzerVisitorsV11(): List<AnalyzerVisitor> {
        val visitors = mutableListOf<AnalyzerVisitor>()
        visitors.add(UnusedVariableVisitor())
        visitors.add(NamingFormatVisitor("camelCase", PatternFactory.getNamingFormatPattern("camelCase")))
        visitors.add(PrintUseVisitor(false))
        visitors.add(ReadInputVisitor(false))
        return visitors
    }

    private fun createAnalyzerVisitorsV10FromJson(jsonFile: JsonObject): List<AnalyzerVisitor> {
        val visitors = mutableListOf<AnalyzerVisitor>()
        val configOnlyKeys = setOf("maxErrors", "enableWarnings", "strictMode")
        for ((key, value) in jsonFile.entrySet()) {
            when (key) {
                "UnusedVariableCheck" -> visitors.add(UnusedVariableVisitor())
                "NamingFormatCheck", "identifierFormat" -> {
                    val namingPatternName =
                        when {
                            key == "identifierFormat" -> {
                                val format = value.asJsonObject.get("format")?.asString ?: "CAMEL_CASE"
                                when (format) {
                                    "CAMEL_CASE" -> "camelCase"
                                    "SNAKE_CASE" -> "snakeCase"
                                    "PASCAL_CASE" -> "pascalCase"
                                    else -> "camelCase"
                                }
                            }
                            else -> value.asJsonObject.get("namingPatternName")?.asString ?: "camelCase"
                        }
                    val pattern = PatternFactory.getNamingFormatPattern(namingPatternName)
                    visitors.add(NamingFormatVisitor(namingPatternName, pattern))
                }
                "PrintUseCheck", "printlnRestrictions" -> {
                    val enabled =
                        when {
                            key == "printlnRestrictions" -> {
                                value.asJsonObject.get("enabled")?.asBoolean == true
                            }
                            else -> value.asJsonObject.get("printlnCheckEnabled")?.asBoolean == true
                        }
                    visitors.add(PrintUseVisitor(enabled))
                }
                in configOnlyKeys -> {
                    // Ignorar propiedades de configuración que no son checks
                }
                else -> throw IllegalArgumentException("Unknown check: $key")
            }
        }
        return visitors
    }

    private fun createAnalyzerVisitorsV11FromJson(jsonFile: JsonObject): List<AnalyzerVisitor> {
        val visitors = mutableListOf<AnalyzerVisitor>()
        val configOnlyKeys = setOf("maxErrors", "enableWarnings", "strictMode")
        for ((key, value) in jsonFile.entrySet()) {
            when (key) {
                "UnusedVariableCheck" -> visitors.add(UnusedVariableVisitor())
                "NamingFormatCheck", "identifierFormat" -> {
                    val namingPatternName =
                        when {
                            key == "identifierFormat" -> {
                                val format = value.asJsonObject.get("format")?.asString ?: "CAMEL_CASE"
                                when (format) {
                                    "CAMEL_CASE" -> "camelCase"
                                    "SNAKE_CASE" -> "snakeCase"
                                    "PASCAL_CASE" -> "pascalCase"
                                    else -> "camelCase"
                                }
                            }
                            else -> value.asJsonObject.get("namingPatternName")?.asString ?: "camelCase"
                        }
                    val pattern = PatternFactory.getNamingFormatPattern(namingPatternName)
                    visitors.add(NamingFormatVisitor(namingPatternName, pattern))
                }
                "PrintUseCheck", "printlnRestrictions" -> {
                    val enabled =
                        when {
                            key == "printlnRestrictions" -> {
                                value.asJsonObject.get("enabled")?.asBoolean == true
                            }
                            else -> value.asJsonObject.get("printlnCheckEnabled")?.asBoolean == true
                        }
                    visitors.add(PrintUseVisitor(enabled))
                }
                "ReadInputCheck" -> {
                    val enabled = value.asJsonObject.get("readInputCheckEnabled")?.asBoolean == true
                    visitors.add(ReadInputVisitor(enabled))
                }
                in configOnlyKeys -> {
                    // Ignorar propiedades de configuración que no son checks
                }
                else -> throw IllegalArgumentException("Unknown check: $key")
            }
        }
        return visitors
    }
}
