package main.kotlin.analyzer

import org.example.astnode.ASTNode
import org.example.iterator.PrintScriptIterator

class AnalyzerFactory {
    fun createAnalyzerV10(nodeIterator: PrintScriptIterator<ASTNode>): DefaultAnalyzer = DefaultAnalyzer("1.0", nodeIterator)

    fun createAnalyzerV11(nodeIterator: PrintScriptIterator<ASTNode>): DefaultAnalyzer = DefaultAnalyzer("1.1", nodeIterator)
}
