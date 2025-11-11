package org.example

import org.example.input.ConsoleInput
import org.example.input.Input
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders

object InterpreterFactory {
    fun createInterpreterVersion10(
        output: Output,
        input: Input = ConsoleInput(),
    ): Interpreter =
        Interpreter(
            output = output,
            input = input,
            provider = PreConfiguredProviders.VERSION_1_0,
        )

    fun createInterpreterVersion11(
        output: Output,
        input: Input = ConsoleInput(),
    ): Interpreter =
        Interpreter(
            output = output,
            input = input,
            provider = PreConfiguredProviders.VERSION_1_1,
        )
}
