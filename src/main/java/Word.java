// This class stores the lexeme and its associated token.
public class Word {
    public Token token; // This Token stores an enum token.
    public String lexeme;   // This String stores a lexeme.
    
    // Initialize token and lexeme with t and l.
    public Word(Token t, String l)
    {
        token = t;
        lexeme = l;
    }
    
    // This method returns a format for how the token and lexeme will be printed.
    @Override
    public String toString()
    {
       return String.format("%-10s => [%s]", token.name(), lexeme);
    }
}
