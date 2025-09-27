package factory

import main.kotlin.parser.DefaultParser

interface ParserFactory {
    fun create(): DefaultParser
}
