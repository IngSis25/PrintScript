package main.kotlin.analyzer

sealed class Types {
    object NUMBER : Types()

    object STRING : Types()

    object BOOLEAN : Types()

    object ARRAY : Types()

    object UNKNOWN : Types()

    object NULL : Types()

    object PROMISE : Types()
}
