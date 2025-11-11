package test.analyzer

import LexerFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.AnalyzerFactory
import org.ParserFactory
import org.example.astnode.ASTNode
import org.junit.jupiter.api.Test
import java.io.File
import java.io.StringReader

class AnalyzerTesterV11 {
    @Test
    fun testSingleWarning() {
        val file = File("src/test/resources/examples-v11/printUseExample.txt")

        val reader = TestReader()
        val (code, expectedWarnings, shouldSucceed) = reader.readTokens(file.path)

        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)

        val jsonContent = File("src/test/jsons/jsonV11.json").readText()
        val jsonObject = Gson().fromJson(jsonContent, JsonObject::class.java)

        compareResults(parser, code, expectedWarnings, shouldSucceed, jsonObject)
    }

    @Test
    fun testMultipleWarnings() {
        val dir = File("src/test/resources/examples-v11")

        dir
            .listFiles { file ->
                file.isFile && file.extension == "txt"
            }?.forEach { file ->
                val reader = TestReader()
                val (code, expectedWarnings, shouldSucceed) = reader.readTokens(file.path)

                val lexer = LexerFactory.createLexerV11(StringReader(code))
                val parser = ParserFactory.createParserV11(lexer)

                val jsonContent = File("src/test/jsons/jsonV11.json").readText()
                val jsonObject = Gson().fromJson(jsonContent, JsonObject::class.java)

                compareResults(parser, code, expectedWarnings, shouldSucceed, jsonObject)
            }
    }

    private fun compareResults(
        parser: org.Parser,
        code: String,
        expectedWarnings: List<String>,
        shouldSucceed: Boolean,
        jsonFile: JsonObject,
    ) {
        // Recolectar todos los nodos AST del parser
        val nodes = mutableListOf<ASTNode>()
        while (parser.hasNext()) {
            nodes.add(parser.next())
        }

        // Crear el analyzer con el config que incluye el JsonObject
        val config = AnalyzerConfig(jsonConfig = jsonFile)

        // Necesitamos crear un nuevo parser con el mismo código porque ya iteramos sobre el anterior
        val lexer2 = LexerFactory.createLexerV11(StringReader(code))
        val parser2 = ParserFactory.createParserV11(lexer2)
        val analyzer = AnalyzerFactory().createAnalyzerV11(parser2)

        // Analizar los nodos
        val result = analyzer.analyze(nodes, config, "1.1")

        if (!shouldSucceed) {
            assert(result.diagnostics.isNotEmpty()) {
                "Expected an error but test passed for code: $code"
            }
        }

        // Comparar warnings esperados con los obtenidos
        val reportList = result.diagnostics.map { it.message }

        expectedWarnings.forEachIndexed { index, expectedWarning ->
            assert(index < reportList.size) {
                "Mismatch in code \"$code\": expected ${expectedWarnings.size} warnings but got ${reportList.size}"
            }
            assert(reportList[index] == expectedWarning) {
                "Mismatch in code \"$code\": " +
                    "expected \"$expectedWarning\", found \"${reportList[index]}\""
            }
        }
    }
}
