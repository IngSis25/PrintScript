package main.kotlin.cli

import java.io.File

sealed class ValidationResult {
    data class Success(
        val file: File,
        val version: String,
        val tokenCount: Int,
        val statementCount: Int,
    ) : ValidationResult()

    data class Error(
        val error: Exception,
        val file: File,
        val version: String,
    ) : ValidationResult()
}
