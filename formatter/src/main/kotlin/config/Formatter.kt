import com.google.gson.Gson
import org.example.ast.ASTNode
import java.io.File

// Punto de entrada único que combina JSON + AST → String formateado

data object Formatter {
    fun format(
        node: ASTNode,
        json: File,
    ): String {
        // 1. Leer y parsear JSON con Gson
        val config = Gson().fromJson(json.readText(Charsets.UTF_8), FormatterConfig::class.java)

        // 2. Crear visitor y procesar
        val result = StringBuilder()
        val visitor = FormatterVisitor(config, result)
        // Procesar AST
        visitor.evaluate(node)

        return result.toString()
    }
}
