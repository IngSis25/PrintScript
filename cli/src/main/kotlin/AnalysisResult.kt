package main.kotlin.cli

import java.io.File

sealed class AnalysisResult {
    data class Success(
        val file: File,
        val version: String,
        val tokenCount: Int,
        val statementCount: Int,
        val analysisResult: main.kotlin.analyzer.AnalysisResult,
        val configFile: File,
    ) : AnalysisResult()

    data class Error(
        val error: Exception,
        val file: File,
        val version: String,
    ) : AnalysisResult()
}
