package org.example.formatter

data class IllegalParameterException(
    override val message: String,
) : IllegalArgumentException(message)
