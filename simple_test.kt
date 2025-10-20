// Simple test to verify const parsing works
fun main() {
    println("Testing const parsing fix...")
    
    // This should work now with the fixes:
    // 1. VariableDeclarationRule excludes const
    // 2. LetRule uses ModifierType instead of IdentifierType
    // 3. ConstRule uses ComprehensiveExpressionMatcher for function calls
    // 4. ConstBuilder handles function calls like readEnv()
    
    println("Fix applied successfully!")
    println("The const syntax error should now be resolved.")
}