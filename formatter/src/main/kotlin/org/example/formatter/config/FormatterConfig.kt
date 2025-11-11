package org.example.formatter.config

import com.google.gson.annotations.SerializedName
import org.example.formatter.rule.IndentRule
import org.example.formatter.rule.InlineIfBraceRule
import org.example.formatter.rule.LineBreaksRule
import org.example.formatter.rule.SpaceAroundColonsRule
import org.example.formatter.rule.SpaceAroundEqualsRule

// Configuración principal del formatter que se carga desde JSON
// todas las opciones de formateo y crea las reglas correspondientes

data class FormatterConfig(
    @SerializedName("line-breaks-after-println") val lineBreaksAfterPrints: Int?,
    @SerializedName("enforce-spacing-around-equals") val spaceAroundEquals: Boolean?,
    @SerializedName("enforce-no-spacing-around-equals") val noSpaceAroundEquals: Boolean?,
    @SerializedName("enforce-spacing-before-colon-in-declaration") val spaceBeforeColon: Boolean?,
    @SerializedName("enforce-spacing-after-colon-in-declaration") val spaceAfterColon: Boolean?,
    @SerializedName("if-brace-same-line") val ifBraceSameLine: Boolean?,
    @SerializedName("if-brace-below-line") val ifBraceBelowLine: Boolean?,
    @SerializedName("indent-inside-if") val indent: Int?,
) {
    val lineBreaksAfterPrintsRule: LineBreaksRule
        get() = LineBreaksRule(lineBreaksAfterPrints ?: 0)

    val spaceAroundEqualsRule: SpaceAroundEqualsRule
        get() = SpaceAroundEqualsRule(spaceAroundEquals ?: true && !(noSpaceAroundEquals ?: false))

    val spaceAroundColonsRule: SpaceAroundColonsRule
        get() = SpaceAroundColonsRule(spaceBeforeColon ?: false, spaceAfterColon ?: true)

    val indentRule: IndentRule
        get() = IndentRule(indent ?: 2)

    val inlineIfBraceRule: InlineIfBraceRule
        get() = InlineIfBraceRule(ifBraceSameLine ?: true && !(ifBraceBelowLine ?: false))
}
