package test.analyzer

import com.google.gson.Gson
import com.google.gson.JsonObject
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.AnalyzerFactory
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import org.example.astnode.ASTNode
import org.junit.jupiter.api.Test
import java.io.File
import java.io.StringReader

class AnalyzerTesterV10 {
    @Test
    fun testSingleWarning() {
        val file = File("src/test/resources/examples-v10/printUseExample.txt")

        val reader = TestReader()
        val (code, expectedWarnings, shouldSucceed) = reader.readTokens(file.path)

        val lexer = LexerFactory.createLexerV10(StringReader(code))
        val parser = ParserFactory.createParserV10(lexer)

        val jsonContent = File("src/test/jsons/jsonV10.json").readText()
        val jsonObject = Gson().fromJson(jsonContent, JsonObject::class.java)

        compareResults(parser, code, expectedWarnings, shouldSucceed, jsonObject)
    }

    @Test
    fun testMultipleWarnings() {
        val dir = File("src/test/resources/examples-v10")

        dir
            .listFiles { file ->
                file.isFile && file.extension == "txt"
            }?.forEach { file ->
                val reader = TestReader()
                val (code, expectedWarnings, shouldSucceed) = reader.readTokens(file.path)

                val lexer = LexerFactory.createLexerV10(StringReader(code))
                val parser = ParserFactory.createParserV10(lexer)

                val jsonContent = File("src/test/jsons/jsonV10.json").readText()
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
        val lexer2 = LexerFactory.createLexerV10(StringReader(code))
        val parser2 = ParserFactory.createParserV10(lexer2)
        val analyzer = AnalyzerFactory().createAnalyzerV10(parser2)

        // Analizar los nodos
        val result = analyzer.analyze(nodes, config, "1.0")

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
