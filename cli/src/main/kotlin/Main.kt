package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption

class PrintScriptCLICommand :
    CliktCommand(
        name = "printscript",
        help = "PrintScript CLI - A language processor for PrintScript files",
    ) {
    override fun run() {
    }
}

fun main(args: Array<String>) {
    PrintScriptCLICommand()
        .subcommands(
            ValidationCommand(),
            ExecutionCommand(),
            FormattingCommand(),
            AnalyzingCommand(),
        ).versionOption("1.0.0")
        .main(args)
}
