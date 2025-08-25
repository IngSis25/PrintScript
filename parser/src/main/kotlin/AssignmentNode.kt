import main.kotlin.parser.IdentifierNode

data class AssignmentNode(
    val identifier: IdentifierNode, // variable a la que asigno
    val value: ASTNode, // valor asignado
) : ASTNode

// asigna un valor a una variable ya creada
