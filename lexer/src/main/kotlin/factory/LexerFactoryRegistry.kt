package factory

// vincula version con la factory que le corresponde
object LexerFactoryRegistry {
    private val factories: Map<String, LexerFactory> = mapOf(
        "1.0" to LexerFactoryV1(),
        "1.1" to LexerFactoryV1_1()
    )

    fun getFactory(version: String): LexerFactory =
        factories[version] ?: throw IllegalArgumentException("Versión no soportada: $version")
}

