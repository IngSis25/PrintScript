package parser

import main.kotlin.parser.ASTNode

data class PrintlnNode(
    val value: ASTNode
) : ASTNode
