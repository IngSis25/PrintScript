package test.analyzer

import com.google.gson.JsonObject
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.AnalyzerFactory
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import org.example.iterator.PrintScriptIterator
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.StringReader

class DefaultAnalyzerTest {
    // Crear un iterador simple para los tests
    private class SimpleIterator<T>(
        private val items: List<T>,
    ) : PrintScriptIterator<T> {
        private var index = 0

        override fun hasNext(): Boolean = index < items.size

        override fun next(): T? = if (index < items.size) items[index++] else null

        override fun peek(): T? = if (index < items.size) items[index] else null
    }

    @Test
    fun testAnalyzeWithEmptyNodes() {
        val nodes = emptyList<org.example.astnode.ASTNode>()
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val config = AnalyzerConfig()
        val result = analyzer.analyze(nodes, config, "1.0")
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeWithMaxErrorsLimit() {
        val code = "let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val config = AnalyzerConfig(maxErrors = 1)
        val result = analyzer.analyze(nodes, config, "1.0")
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeWithMaxErrorsZero() {
        val code = "let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val config = AnalyzerConfig(maxErrors = 0)
        val result = analyzer.analyze(nodes, config, "1.0")
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeWithVersion10() {
        val code = "let unused: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val config = AnalyzerConfig()
        val result = analyzer.analyze(nodes, config, "1.0")
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeWithVersion11() {
        val code = "let unused: number = 10;"
        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV11(iterator)
        val config = AnalyzerConfig()
        val result = analyzer.analyze(nodes, config, "1.1")
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeWithJsonConfig() {
        val code = "let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val jsonConfig = JsonObject()
        val unusedVarCheck = JsonObject()
        jsonConfig.add("UnusedVariableCheck", unusedVarCheck)
        val config = AnalyzerConfig(jsonConfig = jsonConfig)
        val result = analyzer.analyze(nodes, config, "1.0")
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeWithDefaultVersion() {
        val code = "let x: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val config = AnalyzerConfig()
        val result = analyzer.analyze(nodes, config)
        assertNotNull(result)
    }

    @Test
    fun testAnalyzeRecursiveVisiting() {
        val code = "let x: number = 1 + 2;"
        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)
        val nodes = mutableListOf<org.example.astnode.ASTNode>()
        while (parser.hasNext()) {
            parser.next()?.let { nodes.add(it) }
        }
        val iterator: PrintScriptIterator<org.example.astnode.ASTNode> = SimpleIterator(nodes)
        val analyzer = AnalyzerFactory().createAnalyzerV10(iterator)
        val config = AnalyzerConfig()
        val result = analyzer.analyze(nodes, config, "1.0")
        assertNotNull(result)
    }
}
