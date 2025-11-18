package org.example.formatter

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatResultTest {
    @Test
    fun testFormatResultToString() {
        val code = "let x: number = 5;"
        val result = FormatResult(code)
        assertEquals(code, result.toString())
        assertEquals(code, result.code)
    }

    @Test
    fun testFormatResultWithEmptyString() {
        val code = ""
        val result = FormatResult(code)
        assertEquals(code, result.toString())
        assertEquals(code, result.code)
    }

    @Test
    fun testFormatResultWithMultipleLines() {
        val code = "let x: number = 5;\nprintln(x);"
        val result = FormatResult(code)
        assertEquals(code, result.toString())
        assertEquals(code, result.code)
    }
}
