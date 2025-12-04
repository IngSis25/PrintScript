package org

import ASTGenerator
import org.example.Lexer.Location
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.ProgramNode
import org.example.iterator.PrintScriptIterator
import semanticAnalysis.SemanticAnalyzer
import structures.Structure

class Parser(
    private val astGenerator: ASTGenerator,
    private val semanticAnalyzer: SemanticAnalyzer,
    private val supportedStructures: List<Structure>,
    private val tokenIterator: PrintScriptIterator<Token>,
) : PrintScriptIterator<ASTNode> {
    private var peekedElement: ASTNode? = null

    // aca lo pasa al ASTGenerator para que genere el nodo AST.
    fun parse(tokenIterator: PrintScriptIterator<Token>): ASTNode {
        val buffer = ArrayList<Token>()

        while (tokenIterator.hasNext()) {
            val token = tokenIterator.next()!!
            buffer.add(token)

            // Si el token pertenece a alguna Structure, delego
            val structure = supportedStructures.find { it.type == token.type }
            if (structure != null) {
                structure.getTokens(tokenIterator, buffer)
                return handleStatement(buffer)
            }
        }

        if (buffer.isNotEmpty()) {
            throw Exception(
                "Unexpected end of input. Missing semicolon or brace at the end of the file.",
            )
        }
        return ProgramNode("EndOfLine", Location(0, 0), emptyList())
    }

    private fun handleStatement(buffer: ArrayList<Token>): ASTNode =
        try {
            val node: ASTNode = astGenerator.generate(buffer, this)
            try {
                semanticAnalyzer.analyze(node)
                node
            } catch (e: Exception) {
                throw Exception("Semantic error: ${e.message}")
            }
        } catch (e: Exception) {
            throw Exception("Syntactic error: ${e.message}")
        }

    override fun hasNext(): Boolean = peekedElement != null || tokenIterator.hasNext()

    override fun next(): ASTNode {
        if (peekedElement != null) {
            val temp = peekedElement!!
            peekedElement = null
            return temp
        }
        // le pide al lexer tokens hasta que arma un ASTNode.
        return parse(tokenIterator)
    }

    override fun peek(): ASTNode? {
        if (hasNext()) {
            if (peekedElement != null) {
                return peekedElement
            }
            peekedElement = next()
            return peekedElement
        }
        return null
    }

    fun collectAllASTNodes(): List<ASTNode> {
        val newParser = Parser(astGenerator, semanticAnalyzer, supportedStructures, tokenIterator)
        val nodes = mutableListOf<ASTNode>()
        while (newParser.hasNext()) {
            nodes.add(newParser.next())
        }
        return nodes
    }
}
