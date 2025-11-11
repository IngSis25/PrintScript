package main.kotlin.analyzer

import com.google.gson.JsonObject

enum class IdentifierFormat {
    CAMEL_CASE,
    SNAKE_CASE,
    PASCAL_CASE,
}

data class AnalyzerConfig(
    val identifierFormat: IdentifierFormatConfig = IdentifierFormatConfig(),
    val printlnRestrictions: PrintlnRestrictionConfig = PrintlnRestrictionConfig(),
    val maxErrors: Int = 100,
    val enableWarnings: Boolean = true,
    val strictMode: Boolean = false,
    val jsonConfig: JsonObject? = null,
)

data class IdentifierFormatConfig(
    val enabled: Boolean = true,
    val format: IdentifierFormat = IdentifierFormat.CAMEL_CASE,
)

data class PrintlnRestrictionConfig(
    val enabled: Boolean = true,
    val allowOnlyIdentifiersAndLiterals: Boolean = true,
)
