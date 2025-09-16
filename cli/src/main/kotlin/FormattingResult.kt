package main.kotlin.cli

import java.io.File

sealed class FormattingResult {
    data class Success(
        val file: File,
        val version: String,
        val tokenCount: Int,
        val statementCount: Int,
        val formattedCode: String,
        val configFile: File,
        val outputFile: File?,
    ) : FormattingResult()

    data class Error(
        val error: Exception,
        val file: File,
        val version: String,
    ) : FormattingResult()
}
