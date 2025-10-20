package ast

import org.example.ast.ASTNode

data class ReadEnvNode(
    val envVarName: String,
) : ASTNode
