import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import org.Format

class CommandLineInterface : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) =
    CommandLineInterface()
        .subcommands(
            Analyze(),
            Format(),
            Validate(),
            Execute(),
        ).main(args)
