package org.example.strategy


object PreConfiguredProviders {
    val VERSION_1_0 = StrategyProvider builder {
        this addStrategy literalStrategy
        this addStrategy identifierStrategy
        this addStrategy variableDeclarationStrategy
        this addStrategy assignmentStrategy
        this addStrategy binaryExpressionStrategy
        this addStrategy printlnStrategy
    }
}
