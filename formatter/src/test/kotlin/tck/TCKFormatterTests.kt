package tck

import com.google.gson.Gson
import org.example.LiteralString
import org.example.ast.*
import org.example.formatter.config.FormatterConfig
import java.io.File
import kotlin.test.*

class TCKFormatterTests {
    @Test
    fun test_assign_no_spacing_surrounding_equals() {
        val jsonContent = """{"enforce-no-spacing-around-equals": true}"""
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("something"),
                varType = "string",
                value = LiteralNode("a really cool thing", LiteralString),
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let something: string=\"a really cool thing\";\n"

        println("=== assign-no-spacing-surrounding-equals ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_assign_spacing_surrounding_equals() {
        val jsonContent = """{"enforce-spacing-around-equals": true}"""
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("something"),
                varType = "string",
                value = LiteralNode("a really cool thing", LiteralString),
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let something: string = \"a really cool thing\";\n"

        println("=== assign-spacing-surrounding-equals ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_enforce_decl_spacing_after_colon() {
        val jsonContent = """{"enforce-spacing-after-colon-in-declaration": true}"""
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("something"),
                varType = "string",
                value = LiteralNode("a really cool thing", LiteralString),
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let something: string = \"a really cool thing\";\n"

        println("=== enforce-decl-spacing-after-colon ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_enforce_decl_spacing_before_colon() {
        val jsonContent = """{"enforce-spacing-before-colon-in-declaration": true}"""
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("something"),
                varType = "string",
                value = LiteralNode("a really cool thing", LiteralString),
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let something :string = \"a really cool thing\";\n"

        println("=== enforce-decl-spacing-before-colon ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_print_0_line_breaks_after() {
        val jsonContent = """{"line-breaks-after-println": 0}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("something"),
                    varType = "string",
                    value = LiteralNode("a really cool thing", LiteralString),
                ),
                PrintlnNode(IdentifierNode("something")),
                PrintlnNode(LiteralNode("in the way she moves", LiteralString)),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected =
            "let something:string = \"a really cool thing\";\nprintln(something);\nprintln(\"in the way " +
                "she moves\");\n"

        println("=== print-0-line-breaks-after ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_print_1_line_breaks_after() {
        val jsonContent = """{"line-breaks-after-println": 1}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("something"),
                    varType = "string",
                    value = LiteralNode("a really cool thing", LiteralString),
                ),
                PrintlnNode(IdentifierNode("something")),
                PrintlnNode(LiteralNode("in the way she moves", LiteralString)),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected =
            "let something:string = \"a really cool thing\";\nprintln(something);\n\nprintln(\"in " +
                "the way she moves\");\n"

        println("=== print-1-line-breaks-after ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_print_2_line_breaks_after() {
        val jsonContent = """{"line-breaks-after-println": 2}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("something"),
                    varType = "string",
                    value = LiteralNode("a really cool thing", LiteralString),
                ),
                PrintlnNode(IdentifierNode("something")),
                PrintlnNode(LiteralNode("in the way she moves", LiteralString)),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected =
            "let something:string = \"a really cool thing\";\nprintln(something);\n\n\nprintln(\"in the" +
                " way she moves\");\n"

        println("=== print-2-line-breaks-after ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    // Helper functions - Simulamos la lógica del FormatterConfigAdapter del TCK
    private fun formatWithJson(
        node: ASTNode,
        jsonContent: String,
    ): String {
        val tempFile = File.createTempFile("tck-config", ".json")
        tempFile.writeText(jsonContent)

        return try {
            // Simular la lógica del FormatterConfigAdapter
            val config = createConfigFromJson(jsonContent)
            val result = StringBuilder()
            val visitor = org.example.formatter.FormatterVisitor(config, result)
            visitor.evaluate(node)
            result.toString()
        } finally {
            tempFile.delete()
        }
    }

    private fun createConfigFromJson(jsonContent: String): FormatterConfig {
        // Simular la lógica del FormatterConfigAdapter
        val jsonMap = Gson().fromJson(jsonContent, Map::class.java) as Map<String, Any>

        var lineBreaks = 0
        var spaceAroundEquals = true
        var spaceBeforeColon = false
        var spaceAfterColon = false
        var spaceAroundAssignment = true

        // Mapear los casos especiales como lo hace el FormatterConfigAdapter
        jsonMap.forEach { (key, value) ->
            when (key) {
                "line-breaks-after-println" -> lineBreaks = (value as Double).toInt()
                "enforce-spacing-around-equals" -> {
                    if (value as Boolean) {
                        spaceAroundEquals = true
                        spaceAfterColon = true // ¡ESTE ES EL TRUCO!
                        spaceAroundAssignment = true
                    }
                }
                "enforce-no-spacing-around-equals" -> {
                    if (value as Boolean) {
                        spaceAfterColon = true // ¡MANTIENE el espacio después de :!
                        spaceAroundAssignment = false
                    }
                }
                "enforce-spacing-after-colon-in-declaration" -> {
                    if (value as Boolean) {
                        spaceAfterColon = true
                    }
                }
                "enforce-spacing-before-colon-in-declaration" -> {
                    if (value as Boolean) {
                        spaceBeforeColon = true
                    }
                }
            }
        }

        return FormatterConfig(
            lineBreaks,
            spaceAroundEquals,
            spaceBeforeColon,
            spaceAfterColon,
            spaceAroundAssignment,
        )
    }

    private fun formatMultipleWithJson(
        nodes: List<ASTNode>,
        jsonContent: String,
    ): String {
        val tempFile = File.createTempFile("tck-config", ".json")
        tempFile.writeText(jsonContent)

        return try {
            val result = StringBuilder()
            val config = createConfigFromJson(jsonContent)
            val visitor = org.example.formatter.FormatterVisitor(config, result)

            visitor.evaluateMultiple(nodes)

            result.toString()
        } finally {
            tempFile.delete()
        }
    }

    // ========================================
    // TESTS PARA ASSIGNMENTS
    // ========================================

    @Test
    fun test_assignment_with_spacing_around_equals() {
        val jsonContent = """{"enforce-spacing-around-equals": true}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVar"),
                    varType = "number",
                    value = null,
                ),
                AssignmentNode(
                    identifier = IdentifierNode("myVar"),
                    value = LiteralNode("42", org.example.LiteralNumber),
                ),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "let myVar: number;\nmyVar = 42;\n"

        println("=== assignment-with-spacing-around-equals ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_assignment_with_no_spacing_around_equals() {
        val jsonContent = """{"enforce-no-spacing-around-equals": true}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVar"),
                    varType = "string",
                    value = null,
                ),
                AssignmentNode(
                    identifier = IdentifierNode("myVar"),
                    value = LiteralNode("hello", LiteralString),
                ),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "let myVar: string;\nmyVar=\"hello\";\n"

        println("=== assignment-with-no-spacing-around-equals ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    // ========================================
    // TESTS PARA EXPRESIONES COMPLEJAS
    // ========================================

    @Test
    fun test_complex_expression_formatting() {
        val jsonContent = """{"enforce-spacing-around-equals": true}"""
        val complexExpression =
            BinaryOpNode(
                left =
                    BinaryOpNode(
                        left = LiteralNode("5", org.example.LiteralNumber),
                        operator = "*",
                        right = LiteralNode("5", org.example.LiteralNumber),
                    ),
                operator = "-",
                right = LiteralNode("8", org.example.LiteralNumber),
            )

        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("result"),
                varType = "number",
                value = complexExpression,
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let result: number = 5 * 5 - 8;\n"

        println("=== complex-expression-formatting ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_string_concatenation_expression() {
        val jsonContent = """{"enforce-spacing-around-equals": true}"""
        val concatenation =
            BinaryOpNode(
                left = LiteralNode("Hello ", LiteralString),
                operator = "+",
                right = LiteralNode("World", LiteralString),
            )

        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("greeting"),
                varType = "string",
                value = concatenation,
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let greeting: string = \"Hello \" + \"World\";\n"

        println("=== string-concatenation-expression ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_mixed_string_number_concatenation() {
        val jsonContent = """{"enforce-spacing-around-equals": true}"""
        val concatenation =
            BinaryOpNode(
                left = LiteralNode("Number: ", LiteralString),
                operator = "+",
                right = LiteralNode("42", org.example.LiteralNumber),
            )

        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("message"),
                varType = "string",
                value = concatenation,
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let message: string = \"Number: \" + 42;\n"

        println("=== mixed-string-number-concatenation ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    // ========================================
    // TESTS PARA ESCENARIOS MIXTOS
    // ========================================

    @Test
    fun test_complete_program_with_line_breaks() {
        val jsonContent = """{"line-breaks-after-println": 1, "enforce-spacing-around-equals": true}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("name"),
                    varType = "string",
                    value = LiteralNode("Alice", LiteralString),
                ),
                VariableDeclarationNode(
                    identifier = IdentifierNode("age"),
                    varType = "number",
                    value = null,
                ),
                AssignmentNode(
                    identifier = IdentifierNode("age"),
                    value = LiteralNode("25", org.example.LiteralNumber),
                ),
                PrintlnNode(IdentifierNode("name")),
                PrintlnNode(IdentifierNode("age")),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "let name: string = \"Alice\";\nlet age: number;\nage = 25;\nprintln(name);\n\nprintln(age);\n"

        println("=== complete-program-with-line-breaks ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_complete_program_no_spacing() {
        val jsonContent = """{"line-breaks-after-println": 0, "enforce-no-spacing-around-equals": true}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("x"),
                    varType = "number",
                    value = LiteralNode("10", org.example.LiteralNumber),
                ),
                VariableDeclarationNode(
                    identifier = IdentifierNode("y"),
                    varType = "number",
                    value = null,
                ),
                AssignmentNode(
                    identifier = IdentifierNode("y"),
                    value =
                        BinaryOpNode(
                            left = IdentifierNode("x"),
                            operator = "*",
                            right = LiteralNode("2", org.example.LiteralNumber),
                        ),
                ),
                PrintlnNode(IdentifierNode("y")),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "let x: number=10;\nlet y: number;\ny=x * 2;\nprintln(y);\n"

        println("=== complete-program-no-spacing ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    // ========================================
    // TESTS PARA CASOS EXTREMOS
    // ========================================

    @Test
    fun test_declaration_without_initialization_formatting() {
        val jsonContent =
            """{"enforce-spacing-before-colon-in-declaration": true, """ +
                """"enforce-spacing-after-colon-in-declaration": true}"""
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("emptyVar"),
                varType = "string",
                value = null,
            )

        val result = formatWithJson(node, jsonContent)
        val expected = "let emptyVar : string;\n"

        println("=== declaration-without-initialization-formatting ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_multiple_println_with_max_line_breaks() {
        val jsonContent = """{"line-breaks-after-println": 2}"""
        val nodes =
            listOf(
                PrintlnNode(LiteralNode("First", LiteralString)),
                PrintlnNode(LiteralNode("Second", LiteralString)),
                PrintlnNode(LiteralNode("Third", LiteralString)),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "println(\"First\");\n\n\nprintln(\"Second\");\n\n\nprintln(\"Third\");\n"

        println("=== multiple-println-with-max-line-breaks ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_nested_expression_assignment() {
        val jsonContent = """{"enforce-spacing-around-equals": true}"""
        val nestedExpression =
            BinaryOpNode(
                left =
                    BinaryOpNode(
                        left = LiteralNode("10", org.example.LiteralNumber),
                        operator = "+",
                        right = LiteralNode("5", org.example.LiteralNumber),
                    ),
                operator = "*",
                right =
                    BinaryOpNode(
                        left = LiteralNode("3", org.example.LiteralNumber),
                        operator = "-",
                        right = LiteralNode("1", org.example.LiteralNumber),
                    ),
            )

        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("complex"),
                    varType = "number",
                    value = null,
                ),
                AssignmentNode(
                    identifier = IdentifierNode("complex"),
                    value = nestedExpression,
                ),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "let complex: number;\ncomplex = 10 + 5 * 3 - 1;\n"

        println("=== nested-expression-assignment ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }

    @Test
    fun test_all_spacing_options_combined() {
        val jsonContent =
            """{"enforce-spacing-before-colon-in-declaration": true, """ +
                """"enforce-spacing-after-colon-in-declaration": true, "enforce-spacing-around-equals": true, """ +
                """"line-breaks-after-println": 1}"""
        val nodes =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("fullSpacing"),
                    varType = "string",
                    value = LiteralNode("test", LiteralString),
                ),
                PrintlnNode(IdentifierNode("fullSpacing")),
                PrintlnNode(LiteralNode("Done", LiteralString)),
            )

        val result = formatMultipleWithJson(nodes, jsonContent)
        val expected = "let fullSpacing : string = \"test\";\nprintln(fullSpacing);\n\nprintln(\"Done\");\n"

        println("=== all-spacing-options-combined ===")
        println("Config: $jsonContent")
        println("Expected: '${expected.replace("\n", "\\n")}'")
        println("Actual  : '${result.replace("\n", "\\n")}'")

        assertEquals(expected, result)
    }
}
