package factory

import DefaultParser

interface ParserFactory {
    fun create(): DefaultParser
}
