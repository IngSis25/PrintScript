package rules

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

// This class is intended to parse their name for the variables to our names, so that any TCK may be implemented
class RulesParser {
    private val gson = Gson()

    fun parse(content: String): JsonObject {
        if (content == "") {
            return JsonObject()
        }
        val theirJson: JsonObject = JsonParser.parseString(content).asJsonObject
        val map = getMapOfTCK()

        val ourJson = JsonObject()
        for ((key, value) in theirJson.entrySet()) {
            if (key in ourFormat) { // If the key is already in our format
                ourJson.add(key, value)
            } else if (key in map) { // If the key isn't in our format, but expected
                ourJson.add(map[key]!!, value)
            }
        }

        return ourJson
    }

    private fun getMapOfTCK(): Map<String, String> {
        // These are ignored because they are non-configurable
        // "mandatory-single-space-separation" , "mandatory-space-surrounding-operations" to "" , "mandatory-line-break-after-statement" to "",
        return mapOf(
            "enforce-spacing-around-equals" to "space_around_equals",
            "enforce-no-spacing-around-equals" to "no_space_around_equals",
            "enforce-spacing-after-colon-in-declaration" to "space_after_colon",
            "enforce-spacing-before-colon-in-declaration" to "space_before_colon",
            "line-breaks-after-println" to "newline_after_println",
            "if-brace-below-line" to "new_line_for_if_brace",
            "if-brace-same-line" to "same_line_for_if_brace",
            "indent-inside-if" to "number_of_spaces_indentation",
        )
    }

    private val ourFormat =
        listOf(
            "space_before_colon",
            "space_after_colon",
            "newline_after_println",
            "newline_before_println",
            "space_around_equals",
            "no_space_around_equals",
            "number_of_spaces_indentation",
            "same_line_for_if_brace",
            "same_line_for_else_brace",
        )
}
