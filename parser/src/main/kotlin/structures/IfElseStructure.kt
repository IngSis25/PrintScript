package structures

import lexer.Location
import org.example.Lexer.Token
import org.example.astnode.PatternFactory
import org.example.iterator.PrintScriptIterator

class IfElseStructure : Structure {
    override val type = "IfToken"

    override fun getTokens(
        tokenIterator: PrintScriptIterator<Token>,
        buffer: ArrayList<Token>,
    ): ArrayList<Token> {
        val stack = ArrayDeque<Token>()
        while (tokenIterator.hasNext()) {
            val token = tokenIterator.next()
            buffer.add(token!!)

            when (token.type) {
                "OpenBrakeToken" -> stack.addLast(token)
                "CloseBrakeToken" -> {
                    stack.removeLast()
                    if (stack.isEmpty()) {
                        if (tokenIterator.hasNext()) {
                            val token = tokenIterator.peek()
                            if (checkIfElse(token)) {
                                buffer.add(
                                    Token(
                                        "Separator",
                                        "",
                                        Location(0, 0),
                                    ),
                                )
                                getTokens(tokenIterator, buffer)
                            }
                        }
                        return buffer
                    }
                }
            }
        }
        return buffer
    }

    override fun checkStructure(str: Structure): Boolean {
        val ifPattern = PatternFactory.getIfWithElsePattern()
        return Regex(ifPattern).matches(str as CharSequence)
    }

    private fun checkIfElse(token: Token?): Boolean {
        // I'm stood in the last brace of the if block. Check if there is an else
        if (token == null) {
            return false
        }
        return token.type == "ElseToken" // Check if the next token is an else token
    }

    fun separateIfElse(tokens: List<Token>): Pair<List<Token>, List<Token>> {
        val ifTokens = ArrayList<Token>()
        val elseTokens = ArrayList<Token>()
        var isIf = true

        for (token in tokens) {
            if (token.type == "Separator") {
                isIf = false
                continue
            }

            if (isIf) {
                ifTokens.add(token)
            } else {
                elseTokens.add(token)
            }
        }

        return Pair(ifTokens, elseTokens)
    }
}
