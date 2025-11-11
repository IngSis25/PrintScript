package main.kotlin.analyzer

import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets

object ConfigLoader {
    private val gson = Gson()

    fun loadFromJson(file: File): AnalyzerConfig {
        if (!file.exists()) {
            throw IllegalArgumentException("Configuration file does not exist: ${file.absolutePath}")
        }
        if (!file.canRead()) {
            throw IllegalArgumentException("Cannot read configuration file: ${file.absolutePath}")
        }

        val json = file.readText(StandardCharsets.UTF_8)
        return try {
            gson.fromJson(json, AnalyzerConfig::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JSON configuration file: ${e.message}")
        }
    }

    fun loadFromJsonString(jsonContent: String): AnalyzerConfig =
        try {
            gson.fromJson(jsonContent, AnalyzerConfig::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JSON configuration: ${e.message}")
        }

    fun loadWithFallback(configPath: String?): AnalyzerConfig =
        try {
            if (configPath != null) {
                loadFromJson(File(configPath))
            } else {
                AnalyzerConfig() // Default values
            }
        } catch (e: Exception) {
            println("Warning: Error loading configuration: ${e.message}")
            println("Using default configuration...")
            AnalyzerConfig()
        }

    fun createDefaultConfig(file: File) {
        val defaultConfig = AnalyzerConfig()
        val json = gson.toJson(defaultConfig)
        file.writeText(json)
        println("Default configuration created: ${file.absolutePath}")
    }

    fun isValidConfigFile(file: File): Boolean =
        try {
            if (!file.exists() || !file.canRead()) {
                false
            } else {
                // Try to load it to validate format
                loadFromJson(file)
                true
            }
        } catch (e: Exception) {
            false
        }
}
