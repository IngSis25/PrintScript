package org.example.output

/**
 * Output implementation for console that writes to System.out
 */
class ConsoleOutput : Output {
    override fun write(msg: String) {
        print(msg)
    }
}
