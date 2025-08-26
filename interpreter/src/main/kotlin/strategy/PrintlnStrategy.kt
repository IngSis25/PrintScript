package org.example.strategy
import org.example.util.Services
import parser.PrintlnNode

val printlnStrategy =
    Strategy<PrintlnNode> { services: Services, node: PrintlnNode ->
        val value = services.visit(services, node.value)
        services.output write value.toString()
        null
    }
