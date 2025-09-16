package main.kotlin.cli

import java.io.File

sealed class ProcessingResult {
    data class Success(
        val sourceCode: String,
        val tokens: List<main.kotlin.lexer.Token>,
        val ast: List<org.example.ast.ASTNode>,
        val file: File,
        val version: String,
    ) : ProcessingResult()

    data class Error(
        val error: Exception,
        val file: File,
        val version: String,
    ) : ProcessingResult()
}
