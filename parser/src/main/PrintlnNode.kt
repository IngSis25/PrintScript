import org.codehaus.groovy.ast.ASTNode

data class PrintlnNode(
    val value: ASTNode
) : ASTNode