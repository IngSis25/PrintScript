import ast.ReadEnvNode
import org.example.output.Output
import org.example.strategy.readEnvStrategy
import org.example.util.Services
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadEnvStrategyTests {
    private val dummyOutput =
        object : Output {
            override fun write(msg: String) {
                // no-op for tests
            }
        }

    private fun baseServices(
        environment: Map<String, String> = emptyMap(),
        context: Map<String, Any?> = emptyMap(),
    ): Services =
        Services(
            context = context,
            output = dummyOutput,
            visit = { _, _ -> null },
            environment = environment,
        )

    @Test
    fun `returns value from provided environment map`() {
        val services = baseServices(environment = mapOf("BEST_FOOTBALL_CLUB" to "San Lorenzo"))
        val result = readEnvStrategy.visit(services, ReadEnvNode("BEST_FOOTBALL_CLUB"))
        assertEquals("San Lorenzo", result)
    }

    @Test
    fun `falls back to context when environment value is missing`() {
        val services = baseServices(context = mapOf("BEST_FOOTBALL_CLUB" to "Fallback Club"))
        val result = readEnvStrategy.visit(services, ReadEnvNode("BEST_FOOTBALL_CLUB"))
        assertEquals("Fallback Club", result)
    }
}
