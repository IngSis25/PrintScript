package test.analyzer

import main.kotlin.analyzer.SourcePosition
import main.kotlin.analyzer.SymbolTable
import main.kotlin.analyzer.Types
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SymbolTableTest {
    @Test
    fun testDeclareSymbol() {
        val table = SymbolTable()
        val position = SourcePosition(1, 1)
        table.declare("x", Types.NUMBER, true, position)
        assertTrue(table.contains("x"))
        assertTrue(table.isDeclared("x"))
    }

    @Test
    fun testGetType() {
        val table = SymbolTable()
        val position = SourcePosition(1, 1)
        table.declare("x", Types.NUMBER, true, position)
        assertEquals(Types.NUMBER, table.getType("x"))
    }

    @Test
    fun testGetTypeUnknown() {
        val table = SymbolTable()
        assertNull(table.getType("unknown"))
    }

    @Test
    fun testContainsInCurrentScope() {
        val table = SymbolTable()
        val position = SourcePosition(1, 1)
        table.declare("x", Types.NUMBER, true, position)
        assertTrue(table.containsInCurrentScope("x"))
        assertFalse(table.containsInCurrentScope("y"))
    }

    @Test
    fun testGetSymbolInfo() {
        val table = SymbolTable()
        val position = SourcePosition(2, 5)
        table.declare("x", Types.STRING, true, position)
        val info = table.getSymbolInfo("x")
        assertNotNull(info)
        assertEquals("x", info?.name)
        assertEquals(Types.STRING, info?.type)
        assertEquals(position, info?.position)
    }

    @Test
    fun testGetSymbolInfoUnknown() {
        val table = SymbolTable()
        assertNull(table.getSymbolInfo("unknown"))
    }

    @Test
    fun testUpdateType() {
        val table = SymbolTable()
        val position = SourcePosition(1, 1)
        table.declare("x", Types.NUMBER, true, position)
        table.updateType("x", Types.STRING)
        assertEquals(Types.STRING, table.getType("x"))
    }

    @Test
    fun testGetAllSymbols() {
        val table = SymbolTable()
        val position1 = SourcePosition(1, 1)
        val position2 = SourcePosition(2, 1)
        table.declare("x", Types.NUMBER, true, position1)
        table.declare("y", Types.STRING, true, position2)
        val allSymbols = table.getAllSymbols()
        assertEquals(2, allSymbols.size)
        assertTrue(allSymbols.containsKey("x"))
        assertTrue(allSymbols.containsKey("y"))
    }

    @Test
    fun testCreateChildScope() {
        val parent = SymbolTable()
        val position = SourcePosition(1, 1)
        parent.declare("x", Types.NUMBER, true, position)
        val child = parent.createChildScope()
        assertTrue(child.contains("x"))
        assertFalse(child.containsInCurrentScope("x"))
    }

    @Test
    fun testChildScopeCanAccessParentSymbols() {
        val parent = SymbolTable()
        val position = SourcePosition(1, 1)
        parent.declare("x", Types.NUMBER, true, position)
        val child = parent.createChildScope()
        assertEquals(Types.NUMBER, child.getType("x"))
    }

    @Test
    fun testChildScopeDeclaresOwnSymbol() {
        val parent = SymbolTable()
        val parentPosition = SourcePosition(1, 1)
        val childPosition = SourcePosition(2, 1)
        parent.declare("x", Types.NUMBER, true, parentPosition)
        val child = parent.createChildScope()
        child.declare("y", Types.STRING, true, childPosition)
        assertTrue(child.contains("x"))
        assertTrue(child.contains("y"))
        assertTrue(child.containsInCurrentScope("y"))
        assertFalse(parent.contains("y"))
    }

    @Test
    fun testGetDepth() {
        val table = SymbolTable()
        assertEquals(1, table.getDepth())
        val child = table.createChildScope()
        assertEquals(2, child.getDepth())
        val grandchild = child.createChildScope()
        assertEquals(3, grandchild.getDepth())
    }

    @Test
    fun testSymbolShadowing() {
        val parent = SymbolTable()
        val parentPosition = SourcePosition(1, 1)
        val childPosition = SourcePosition(2, 1)
        parent.declare("x", Types.NUMBER, true, parentPosition)
        val child = parent.createChildScope()
        child.declare("x", Types.STRING, true, childPosition)
        assertEquals(Types.STRING, child.getType("x"))
        assertEquals(Types.NUMBER, parent.getType("x"))
    }

    @Test
    fun testDeclareMultipleTimesSameSymbol() {
        val table = SymbolTable()
        val position1 = SourcePosition(1, 1)
        val position2 = SourcePosition(2, 1)
        table.declare("x", Types.NUMBER, true, position1)
        table.declare("x", Types.STRING, true, position2)
        // La segunda declaración no debería sobrescribir
        val info = table.getSymbolInfo("x")
        assertEquals(Types.NUMBER, info?.type)
    }

    @Test
    fun testGetMethod() {
        val table = SymbolTable()
        val position = SourcePosition(1, 1)
        table.declare("x", Types.BOOLEAN, true, position)
        assertEquals(Types.BOOLEAN, table.get("x"))
    }

    @Test
    fun testGetMethodUnknown() {
        val table = SymbolTable()
        assertNull(table.get("unknown"))
    }

    @Test
    fun testIsMutable() {
        val table = SymbolTable()
        val position = SourcePosition(1, 1)
        table.declare("x", Types.NUMBER, true, position)
        table.declare("y", Types.STRING, false, position)
        val infoX = table.getSymbolInfo("x")
        val infoY = table.getSymbolInfo("y")
        assertTrue(infoX?.isMutable == true)
        assertTrue(infoY?.isMutable == false)
    }

    @Test
    fun testMultipleScopes() {
        val global = SymbolTable()
        global.declare("global", Types.NUMBER, true, SourcePosition(1, 1))
        val function1 = global.createChildScope()
        function1.declare("local1", Types.STRING, true, SourcePosition(2, 1))
        val function2 = global.createChildScope()
        function2.declare("local2", Types.BOOLEAN, true, SourcePosition(3, 1))
        assertTrue(function1.contains("global"))
        assertTrue(function1.contains("local1"))
        assertFalse(function1.contains("local2"))
        assertTrue(function2.contains("global"))
        assertFalse(function2.contains("local1"))
        assertTrue(function2.contains("local2"))
    }
}
