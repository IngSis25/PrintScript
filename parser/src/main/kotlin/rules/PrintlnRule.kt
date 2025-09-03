package rules

import builders.NodeBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.*
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.PunctuationType

class PrintlnRule(
    override val builder: NodeBuilder,
) : ParserRule {
    // para reconocer println ( ... )
    override val matcher: Matcher<List<Token>> =
        object : Matcher<List<Token>> {
            override fun match(
                tokens: List<Token>,
                pos: Int,
            ): ParseResult<List<Token>>? {
                // minimo necesito 5 tokens
                if (pos + 4 >= tokens.size) return null

                // debe empezar con "println"
                val t0 = tokens[pos]
                if (t0.type != IdentifierType || t0.value != "println") return null

                // debe seguir un "("
                val t1 = tokens[pos + 1]
                if (t1.type != PunctuationType || t1.value != "(") return null

                // empiezo a buscar el parentesis de cierre, teniendo en cuenta que puede haber parentesis adentro
                var i = pos + 2
                var depth = 1
                while (i < tokens.size) {
                    val ti = tokens[i]
                    if (ti.type == PunctuationType && ti.value == "(") depth++
                    if (ti.type == PunctuationType && ti.value == ")") {
                        depth--
                        if (depth == 0) break // encontre el parentesis de cierre, se termina la sentencia
                    }
                    i++
                }
                if (depth != 0) return null // no encontre el parentesis de cierre

                val closeParenIndex = i

                // despues del parentesis de cierre tiene que haber un ;
                val semiIndex = closeParenIndex + 1
                if (semiIndex >= tokens.size) return null
                val semi = tokens[semiIndex]
                if (semi.type != PunctuationType || semi.value != ";") return null

                val matchedSlice = tokens.subList(pos, semiIndex + 1)
                return ParseResult.Success(matchedSlice, semiIndex + 1)
            }
        }
}
