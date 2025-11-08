package org.example.strategy

object PreConfiguredProviders {
    val VERSION_1_0 =
        StrategyProvider builder {
            this addStrategy literalStrategy
            this addStrategy identifierStrategy
            this addStrategy variableDeclarationStrategy
            this addStrategy assignmentStrategy
            this addStrategy binaryExpressionStrategy
            this addStrategy printStatementStrategy
        }

    val VERSION_1_1 =
        VERSION_1_0.plus(
            StrategyProvider builder {
                this addStrategy ifStrategy
                this addStrategy completeIfStrategy
                this addStrategy booleanExpressionStrategy
                this addStrategy readEnvStrategy
                this addStrategy readInputStrategy
            },
        )
}
