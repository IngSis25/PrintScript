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
        )

    @Test
    fun `returns value from provided environment map`() {
        val services = baseServices(environment = mapOf("BEST_FOOTBALL_CLUB" to "San Lorenzo"))
        val result = readEnvStrategy.visit(services, ReadEnvNode("BEST_FOOTBALL_CLUB"))
        assertEquals("San Lorenzo", result)
    }
}
