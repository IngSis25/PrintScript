package main.kotlin.cli

import java.io.File

sealed class ExecutionResult {
    data class Success(
        val file: File,
        val version: String,
        val tokenCount: Int,
        val statementCount: Int,
    ) : ExecutionResult()

    data class Error(
        val error: Exception,
        val file: File,
        val version: String,
    ) : ExecutionResult()
}
