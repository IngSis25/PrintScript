package services

/**
 * Service for output operations - makes CLI testable
 */
interface OutputService {
    fun println(message: String)

    fun printError(message: String)

    fun printProgress(message: String)

    fun printSuccess(message: String)
}

/**
 * Default implementation for production use
 */
class DefaultOutputService : OutputService {
    override fun println(message: String) {
        kotlin.io.println(message)
    }

    override fun printError(message: String) {
        System.err.println("❌ ERROR: $message")
    }

    override fun printProgress(message: String) {
        kotlin.io.println("⚡ $message")
    }

    override fun printSuccess(message: String) {
        kotlin.io.println("✅ $message")
    }
}
