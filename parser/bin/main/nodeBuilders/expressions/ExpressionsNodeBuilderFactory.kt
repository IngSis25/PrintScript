package nodeBuilders.expressions

import ASTNodeBuilder

class ExpressionsNodeBuilderFactory {
    fun createV10(): List<ASTNodeBuilder> =
        listOf<ASTNodeBuilder>(
            BinaryExpressionNodeBuilder(),
        )

    fun createV11(): List<ASTNodeBuilder> =
        listOf<ASTNodeBuilder>(
            BinaryExpressionNodeBuilder(),
            ReadEnvNodeBuilder(),
            ReadInputNodeBuilder(),
            BooleanExpressionNodeBuilder(),
        )
}
