package factory

object ParserFactoryRegistry {
    private val factories: Map<String, ParserFactory> =
        mapOf(
            "1.0" to ParserFactoryV1(),
            "1.1" to ParserFactoryV11(),
        )

    fun getFactory(version: String): ParserFactory =
        factories[version] ?: throw IllegalArgumentException("Versión no soportada: $version")
}
