package org.example

import main.kotlin.analyzer.SourcePosition
import main.kotlin.analyzer.SymbolTable
import main.kotlin.analyzer.Types
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SymbolTableTest {
    @Test
    fun `should declare and retrieve variables`() {
        val table =
            SymbolTable()
                .declare("x", Types.NUMBER)
                .declare("y", Types.STRING)

        assertTrue(table.contains("x"))
        assertTrue(table.contains("y"))
        assertFalse(table.contains("z"))

        assertEquals(Types.NUMBER, table.get("x"))
        assertEquals(Types.STRING, table.get("y"))
        assertNull(table.get("z"))
    }

    @Test
    fun `should not allow duplicate declarations in same scope`() {
        val table =
            SymbolTable()
                .declare("x", Types.NUMBER)
                .declare("x", Types.STRING) // This should not override

        assertEquals(Types.NUMBER, table.get("x"))
    }

    @Test
    fun `should support scoping with parent-child relationships`() {
        val parent = SymbolTable().declare("x", Types.NUMBER)
        val child = parent.createChildScope().declare("y", Types.STRING)

        // Child should see parent's variables
        assertTrue(child.contains("x"))
        assertTrue(child.contains("y"))
        assertEquals(Types.NUMBER, child.get("x"))
        assertEquals(Types.STRING, child.get("y"))

        // Parent should not see child's variables
        assertTrue(parent.contains("x"))
        assertFalse(parent.contains("y"))
    }

    @Test
    fun `should handle variable shadowing`() {
        val parent = SymbolTable().declare("x", Types.NUMBER)
        val child = parent.createChildScope().declare("x", Types.STRING)

        // Child should see its own version of x
        assertEquals(Types.STRING, child.get("x"))

        // Parent should still see its version
        assertEquals(Types.NUMBER, parent.get("x"))
    }

    @Test
    fun `should track symbol information`() {
        val location = SourcePosition(10, 5)
        val table = SymbolTable().declare("x", Types.NUMBER, true, location)

        val symbolInfo = table.getSymbolInfo("x")
        assertNotNull(symbolInfo)
        assertEquals(Types.NUMBER, symbolInfo!!.type)
        assertTrue(symbolInfo.isMutable)
        assertEquals(location, symbolInfo.declaredAt)
    }

    @Test
    fun `should distinguish between current scope and inherited symbols`() {
        val parent = SymbolTable().declare("x", Types.NUMBER)
        val child = parent.createChildScope().declare("y", Types.STRING)

        assertTrue(child.containsInCurrentScope("y"))
        assertFalse(child.containsInCurrentScope("x"))

        assertTrue(child.contains("x")) // Inherited
        assertTrue(child.contains("y")) // Current scope
    }

    @Test
    fun `should get all symbols including inherited ones`() {
        val parent =
            SymbolTable()
                .declare("x", Types.NUMBER)
                .declare("y", Types.STRING)

        val child =
            parent
                .createChildScope()
                .declare("z", Types.NUMBER)
                .declare("x", Types.STRING) // Shadows parent's x

        val allSymbols = child.getAllSymbols()

        assertEquals(3, allSymbols.size)
        assertTrue(allSymbols.containsKey("x"))
        assertTrue(allSymbols.containsKey("y"))
        assertTrue(allSymbols.containsKey("z"))

        // Child's x should override parent's x
        assertEquals(Types.STRING, allSymbols["x"]!!.type)
        assertEquals(Types.STRING, allSymbols["y"]!!.type)
        assertEquals(Types.NUMBER, allSymbols["z"]!!.type)
    }
}
