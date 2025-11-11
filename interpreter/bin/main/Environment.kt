package org.example

private const val PI = 3.1415

private const val GRAVITY = 9.81

internal object Environment {
    private val globalVariables =
        mapOf(
            "gravity" to GRAVITY,
            "pi" to PI,
            "BEST_FOOTBALL_CLUB" to "San Lorenzo",
        )

    fun getGlobalVariable(name: String): Any? = globalVariables[name]

    fun hasGlobalVariable(name: String): Boolean = globalVariables.containsKey(name)
}
