package builders

import commands.Command
import services.FileService
import services.OutputService

/**
 * Base interface for all command builders
 */
interface CommandBuilder<T : Command> {
    fun withFile(filePath: String): CommandBuilder<T>

    fun withVersion(version: String): CommandBuilder<T>

    fun withFileService(fileService: FileService): CommandBuilder<T>

    fun withOutputService(outputService: OutputService): CommandBuilder<T>

    fun build(): T
}

/**
 * Abstract base class for command builders with common functionality
 */
abstract class BaseCommandBuilder<T : Command> : CommandBuilder<T> {
    protected var filePath: String? = null
    protected var version: String = "1.0"
    protected var fileService: FileService? = null
    protected var outputService: OutputService? = null

    override fun withFile(filePath: String): CommandBuilder<T> {
        this.filePath = filePath
        return this
    }

    override fun withVersion(version: String): CommandBuilder<T> {
        this.version = version
        return this
    }

    override fun withFileService(fileService: FileService): CommandBuilder<T> {
        this.fileService = fileService
        return this
    }

    override fun withOutputService(outputService: OutputService): CommandBuilder<T> {
        this.outputService = outputService
        return this
    }

    protected fun validateRequired() {
        require(filePath != null) { "File path is required" }
        require(fileService != null) { "File service is required" }
        require(outputService != null) { "Output service is required" }
    }
}
