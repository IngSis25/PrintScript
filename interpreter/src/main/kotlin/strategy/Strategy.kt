package org.example.strategy

import ASTNode
import org.example.util.Services
//interfaz funcional = a solo metodo abstracto
fun interface Strategy<T : ASTNode>
{
    fun visit(services: Services, node: T): Any?
}
