import nodeBuilders.AssignmentNodeBuilder
import nodeBuilders.ExpressionNodeBuilder
import nodeBuilders.IdentifierNodeBuilder
import nodeBuilders.IfNodeBuilder
import nodeBuilders.PrintNodeBuilder
import nodeBuilders.VariableDeclarationNodeBuilder
import nodeBuilders.expressions.ExpressionsNodeBuilderFactory
import org.Parser
import org.example.Lexer.Token
import org.example.iterator.PrintScriptIterator
import semanticAnalysis.SemanticAnalyzer
import semanticAnalysis.check.AssignmentKindCheck
import semanticAnalysis.check.AssignmentTypeCheck
import semanticAnalysis.check.CompleteIfBooleanExpressionCheck
import semanticAnalysis.check.IfBooleanExpressionCheck
import semanticAnalysis.check.ReadInputTypeCheck
import semanticAnalysis.check.SemanticCheck
import semanticAnalysis.check.VariableDeclarationCheck
import semanticAnalysis.check.VariableDeclarationTypeCheck
import structures.IfElseStructure

object ParserFactory {
    fun createParserV10(tokenIterator: PrintScriptIterator<Token>): Parser =
        Parser(
            astGenerator = createASTGeneratorV10(),
            semanticAnalyzer = createSemanticAnalyzerV10(),
            supportedStructures = emptyList(),
            tokenIterator = tokenIterator,
        )

    fun createParserV11(tokenIterator: PrintScriptIterator<Token>): Parser =
        Parser(
            astGenerator = createASTGeneratorV11(),
            semanticAnalyzer = createSemanticAnalyzerV11(),
            supportedStructures = listOf(IfElseStructure()),
            tokenIterator = tokenIterator,
        )

    private fun createASTGeneratorV10(): ASTGenerator =
        ASTGenerator(
            listOf(
                VariableDeclarationNodeBuilder(),
                PrintNodeBuilder(),
                AssignmentNodeBuilder(),
                ExpressionNodeBuilder(ExpressionsNodeBuilderFactory().createV10()),
                IdentifierNodeBuilder(),
            ),
        )

    private fun createASTGeneratorV11(): ASTGenerator =
        ASTGenerator(
            listOf(
                VariableDeclarationNodeBuilder(),
                PrintNodeBuilder(),
                AssignmentNodeBuilder(),
                ExpressionNodeBuilder(),
                IdentifierNodeBuilder(),
                IfNodeBuilder(),
            ),
        )

    private fun createSemanticAnalyzerV10(): SemanticAnalyzer {
        val checks: List<SemanticCheck> =
            listOf(
                VariableDeclarationCheck(),
                AssignmentTypeCheck(),
                VariableDeclarationTypeCheck(),
            )
        return SemanticAnalyzer(checks)
    }

    private fun createSemanticAnalyzerV11(): SemanticAnalyzer {
        val checks: List<SemanticCheck> =
            listOf(
                VariableDeclarationCheck(),
                AssignmentTypeCheck(),
                VariableDeclarationTypeCheck(),
                AssignmentKindCheck(),
                ReadInputTypeCheck(),
                CompleteIfBooleanExpressionCheck(),
                IfBooleanExpressionCheck(),
            )
        return SemanticAnalyzer(checks)
    }
}
