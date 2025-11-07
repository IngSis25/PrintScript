package builders

import ast.ReadEnvNode
import main.kotlin.lexer.Token
import org.example.ast.ASTNode

class ReadEnvBuilder : NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        require(matchedTokens.size == 4) {
            "readEnv(...) debe tener exactamente 1 argumento string"
        }

        val start = matchedTokens.first()

        // 0: "readEnv"
        val readEnvTok = matchedTokens[0]
        check(readEnvTok.value == "readEnv") {
            "Se esperaba 'readEnv' pero llegó '${readEnvTok.value}'"
        }

        // 1: "(" y 3: ")"
        val openTok = matchedTokens[1]
        val closeTok = matchedTokens[3]
        check(openTok.value == "(") { "Se esperaba '(' luego de readEnv" }
        check(closeTok.value == ")") { "Se esperaba ')' al final de readEnv(...)" }

        // 2: STRING literal
        val argTok = matchedTokens[2]
        check(argTok.type == LiteralString) {
            "readEnv espera un literal string, llegó: ${argTok.type} '${argTok.value}'"
        }

        val envName = argTok.value.trim('"')
        return ReadEnvNode(envName)
    }
}
