package org.example

import org.example.output.Output
import org.example.strategy.PreConfiguredProviders

object InterpreterFactory {
    fun createInterpreterVersion10(output: Output): Interpreter =
        Interpreter(
            output = output,
            provider = PreConfiguredProviders.VERSION_1_0,
        )

    fun createInterpreterVersion11(output: Output): Interpreter =
        Interpreter(
            output = output,
            provider = PreConfiguredProviders.VERSION_1_1,
        )
}
