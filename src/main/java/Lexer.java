// Import ArrayList and the regex Matcher and Pattern
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// This class acts as the lexer, with some help from Token.java and Word.java.
public class Lexer {
    // This method takes a String and goes through it to find the tokens in it.
    // The method returns an arraylist of Words, a class containing a token and lexeme.
    public static ArrayList<Word> lex(String input)
    {
        // Create a new ArrayList
        ArrayList<Word> words = new ArrayList<>();

        // Create a new Stringbuffer.
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (Token token : Token.values())
        {
            // Append every token to a list of tokens.
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", token.name(), token.pattern));
        }
        // Create a new Pattern that compiles the list of tokens
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) 
        {
            for (Token token : Token.values())
            {
                // While there's a match, for every token, if the matcher group exists,
                // add the tokens to the arraylist of words.
                if (matcher.group(token.name()) != null) 
                {
                    words.add(new Word(token, matcher.group(token.name())));
                }
            }
        }
        // Return the arraylist of words.
        return words;
    }
}
