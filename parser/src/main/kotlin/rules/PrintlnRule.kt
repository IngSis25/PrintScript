package rules

import main.kotlin.lexer.*
import main.kotlin.parser.*
import parser.PrintlnNode
import parser.matchers.Matcher
import parser.rules.ParserRule

class PrintlnRule : ParserRule {

    //para reconocer println ( ... )
    override val matcher: Matcher<List<Token>> = object : Matcher<List<Token>> {
        override fun match(tokens: List<Token>, pos: Int): ParseResult<List<Token>>? {

            // minimo necesito 5 tokens
            if (pos + 4 >= tokens.size) return null

            // debe empezar con "println"
            val t0 = tokens[pos]
            if (t0.type != IdentifierType || t0.value != "println") return null

            // debe seguir un "("
            val t1 = tokens[pos + 1]
            if (t1.type != PunctuationType || t1.value != "(") return null

            //empiezo a buscar el parentesis de cierre, teniendo en cuenta que puede haber parentesis adentro
            var i = pos + 2
            var depth = 1
            while (i < tokens.size) {
                val ti = tokens[i]
                if (ti.type == PunctuationType && ti.value == "(") depth++
                if (ti.type == PunctuationType && ti.value == ")") {
                    depth--
                    if (depth == 0) break //encontre el parentesis de cierre, se termina la sentencia
                }
                i++
            }
            if (depth != 0) return null //no encontre el parentesis de cierre

            val closeParenIndex = i

            //despues del parentesis de cierre tiene que haber un ;
            val semiIndex = closeParenIndex + 1
            if (semiIndex >= tokens.size) return null
            val semi = tokens[semiIndex]
            if (semi.type != PunctuationType || semi.value != ";") return null

            val matchedSlice = tokens.subList(pos, semiIndex + 1)
            return ParseResult.Success(matchedSlice, semiIndex + 1)
        }
    }

    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val openParenIndex = 1
        val closeParenIndex = matchedTokens.lastIndex - 1
        val inner = matchedTokens.subList(openParenIndex + 1, closeParenIndex)

        val exprNode = parseInnerExpression(inner)
        return PrintlnNode(exprNode)
    }


    //ahora tengo que convertir los tokens internos en un nodo de EXPRESION
    private fun parseInnerExpression(tokens: List<Token>): ASTNode {
        // Caso simple: UN token (literal o identificador)
        if (tokens.size == 1) return tokenToPrimary(tokens[0])

        // Caso binario: 3 tokens => A op B (ej: 12 + 8)
        if (tokens.size == 3 && tokens[1].type == OperatorType) {
            val left = tokenToPrimary(tokens[0])
            val op = tokens[1].value
            val right = tokenToPrimary(tokens[2])
            return BinaryOpNode(left, op, right)
        }
        error("Expresión no soportada dentro de println por ahora")
    }

    private fun tokenToPrimary(token: Token) : ASTNode =
        when (token.type) {
            LiteralString -> LiteralNode(unquote(token.value), LiteralString)
            LiteralNumber -> LiteralNode(token.value, LiteralNumber)
            IdentifierType -> IdentifierNode(token.value)
            else -> error("Token no válido en println: ${token.type}")
        }

    private fun unquote(raw: String): String =
        if (raw.length >= 2 && raw.first() == '"' && raw.last() == '"')
            raw.substring(1, raw.length - 1) else raw

}