package services

/**
 * Service for file operations - makes CLI testable
 */
interface FileService {
    fun readFile(path: String): String

    fun exists(path: String): Boolean

    fun writeFile(
        path: String,
        content: String,
    )
}

/**
 * Default implementation for production use
 */
class DefaultFileService : FileService {
    override fun readFile(path: String): String = java.io.File(path).readText()

    override fun exists(path: String): Boolean = java.io.File(path).exists()

    override fun writeFile(
        path: String,
        content: String,
    ) {
        java.io.File(path).writeText(content)
    }
}
