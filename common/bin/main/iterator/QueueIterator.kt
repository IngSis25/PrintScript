package org.example.iterator

import org.example.Lexer.Token
import java.util.LinkedList

class QueueIterator(
    list: List<Token>,
) : PrintScriptIterator<Token> {
    private val queue = LinkedList(list)

    override fun hasNext(): Boolean = queue.isNotEmpty()

    override fun next(): Token? {
        if (hasNext()) {
            return queue.pop()
        }
        return null
    }

    override fun peek(): Token? = queue.peek()
}
