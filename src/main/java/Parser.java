// Import the stack.
import java.util.Stack;
// This class takes a word and parses it using a stack.
public class Parser {
    private Stack<Token> s = new Stack<>(); // Stores a stack of tokens.
    private Stack<String> l = new Stack<>();    // Stores a stack of lexemes, to be used in an interpreter.
    private Token t;    // Stores a Token for comparing enum fields.
    private Stack<Token> interpretS = new Stack<>();
    private Stack<String> interpretL = new Stack<>();
    private Interpreter m = new Interpreter();
    private boolean loop = false;
    private Stack<Token> sLoop = new Stack<>();
    private Stack<String> lLoop = new Stack<>();
    private Stack<String> condition = new Stack<>();
    
    // This method checks if the stack is empty.
    // The method returns true if so, false otherwise.
    public boolean isEmpty()
    {
        boolean empty = false;
        
        if (s.empty())
        {
            // If the stack is empty, make note of it.
            empty = true;
        }
        
        return empty;
    }
    
    // This method clears the stacks.
    public void clean()
    {
        while (!isEmpty())
        {
            // Clear the stacks until they're empty.
            s.pop();
            l.pop();
        }
    }
    
    public void cleanInterpreter()
    {
        while (!interpretS.empty())
        {
            interpretS.pop();
        }
        
        while (!interpretL.empty())
        {
            interpretL.pop();
        }
    }
    
    public void cleanLoop()
    {
        while (!sLoop.empty())
        {
            sLoop.pop();
        }
        
        while (!lLoop.empty())
        {
            lLoop.pop();
        }
    }
    
    // This method parses a word.
    // If there are no errors, return true. Otherwise, return false.
    public boolean parse(Word w)
    {
        boolean result = true;
        switch(w.token)
        {
            case KEYWORDPRINT:
            case KEYWORDGET:
            case KEYWORDIF:
            case KEYWORDWHILE:
            case KEYWORDFOR:
                // For keywords that start the line, the stack should be empty
                // unless in the middle of a block statement.
                if (!isEmpty())
                {
                    if (s.peek() != t.KEYWORDTHEN && s.peek() != t.KEYWORDDO)
                    {
                        result = false;
                        System.out.println("ERROR: Stack is not empty.");
                        clean();
                        cleanLoop();
                        loop = false;
                        break;
                    }
                }
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case KEYWORDTHEN:
                // For the then keyword, the stack shouldn't be empty.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    break;
                }
                
                // The stack should contain the keyword if.
                if (s.search(t.KEYWORDIF) < 0)
                {
                    result = false;
                    System.out.println("ERROR: Keyword if doesn't exist.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // There must be a condition separating then from if.
                if (s.peek() == t.KEYWORDIF)
                {
                    result = false;
                    System.out.println("ERROR: No condition statement.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case KEYWORDDO:
                // For the do keyword, the stack shouldn't be empty.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    break;
                }
                
                // Either the while keyword or the for keyword must be on the stack.
                if (s.search(t.KEYWORDWHILE) < 0 && s.search(t.KEYWORDFOR) < 0)
                {
                    result = false;
                    System.out.println("ERROR: Keyword while and keyword for don't exist.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // There must at least be a condition separating do from while or for.
                if (s.peek() == t.KEYWORDWHILE || s.peek() == t.KEYWORDFOR)
                {
                    result = false;
                    System.out.println("ERROR: No condition statement.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                loop = true;
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case KEYWORDEACH:
            case KEYWORDIN:
                // For the each and in keywords, the stack shouldn't be empty.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    break;
                }
                
                // The for keyword must be on the stack.
                if (s.search(t.KEYWORDFOR) < 0)
                {
                    result = false;
                    System.out.println("ERROR: Keyword for doesn't exist.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                break;
            case KEYWORDNOT:
            case KEYWORDAND:
            case KEYWORDOR:
            case OPERATORLEQ:
            case OPERATORGEQ:
            case OPERATOREQ:
            case OPERATORNEQ:
            case OPERATORASSIGN:
            case OPERATORADD:
            case OPERATORMIN:
            case OPERATORMUL:
            case OPERATORDIV:
            case OPERATORMOD:
            case OPERATORGT:
            case OPERATORLT:
            case TOKENCOMMA:
            case TOKENLPAR:
            case TOKENLBRACK:
            case TOKENLBRACE:
            case TOKENINT:
                // For logic keywords, operators, commas, and left parentheses, left
                // brackets, and left curly braces, the stack must not be empty,
                // as they depend on the existence of a keyword or token to
                // their left.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case TOKENRPAR:
                // For the right parenthesis, the stack shouldn't be empty,
                // and the matching parenthesis must exist.
                if (isEmpty() || s.search(t.TOKENLPAR) < 0)
                {
                    result = false;
                    System.out.println("ERROR: No matching symbol found.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case TOKENRBRACK:
                // For the right brackets, the stack shouldn't be empty,
                // and the matching bracket must exist.
                if (isEmpty() || s.search(t.TOKENLBRACK) < 0)
                {
                    result = false;
                    System.out.println("ERROR: No matching symbol found.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case TOKENRBRACE:
                // For the right curly brace, the stack shouldn't be empty,
                // and the matching curly brace must exist.
                if (isEmpty() || s.search(t.TOKENLBRACE) < 0)
                {
                    result = false;
                    System.out.println("ERROR: No matching symbol found.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case TOKENID:
                // IDs are simply pushed onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case TOKENSTRING:
                // For strings, the stack shouldn't be empty, and they should
                // follow the print keyword.
                if (isEmpty() || s.peek() != t.KEYWORDPRINT)
                {
                    result = false;
                    System.out.println("ERROR: Keyword print doesn't exist.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case KEYWORDELSE:
                // For the else keyword, the stack shouldn't be empty.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    break;
                }
                
                // The if and then keywords must be on the stack.
                if (s.search(t.KEYWORDIF) < 0 && s.search(t.KEYWORDTHEN) < 0)
                {
                    result = false;
                    System.out.println("ERROR: Keywords if, then don't exist.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                if (loop == true)
                {
                    sLoop.push(w.token);
                    lLoop.push(w.lexeme);
                }
                break;
            case KEYWORDEND:
                // For the end keyword, the stack shouldn't be empty.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    break;
                }
                
                // The keyword must come at the end of a block statement.
                if (s.search(t.KEYWORDIF) < 0 && s.search(t.KEYWORDWHILE) < 0 && s.search(t.KEYWORDFOR) < 0)
                {
                    result = false;
                    System.out.println("ERROR: No block statement.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                // There must be a then or do keyword on the stack.
                if (s.search(t.KEYWORDTHEN) < 0 && s.search(t.KEYWORDDO) < 0)
                {
                    result = false;
                    System.out.println("ERROR: No keyword then/do.");
                    clean();
                    cleanLoop();
                    loop = false;
                    break;
                }
                
                Token x;
                boolean thenX = false;
                String lex;
                // Pop the stack until the if, while, or for keywords are popped.
                do
                {
                    x = s.pop();
                    lex = l.pop();
                    
                    if (loop == true)
                    {
                        interpretS.push(x);
                        interpretL.push(lex);
                        
                        if (thenX == true)
                        {
                            if (!lex.equals("while"))
                            {
                                condition.push(lex);
                            }
                            
                        }
                    }
                    
                    if (x == t.KEYWORDDO)
                    {
                        thenX = true;
                    }
                }
                while (x != t.KEYWORDIF && x != t.KEYWORDWHILE && x != t.KEYWORDFOR);
                
                if (loop == true)
                {
                    while (m.evaluate(condition) != 0)
                    {
                        m.interpret(interpretS, interpretL);
                    }
                }
                cleanInterpreter();
                cleanLoop();
                
                // Push the word onto the stack.
                s.push(w.token);
                l.push(w.lexeme);
                break;
            case TOKENSEMI:
                // For the semicolon, the stack shouldn't be empty.
                if (isEmpty())
                {
                    result = false;
                    System.out.println("ERROR: Stack is empty.");
                    break;
                }
                
                Token y;
                Token eq = null;
                // Pop the stack until it is empty or otherwise stated.
                loop: do
                {
                    y = s.pop();
                    interpretS.push(y);
                    interpretL.push(l.pop());
                    
                    // If an assignment has taken place, stop popping.
                    if (eq == t.OPERATORASSIGN && y == t.TOKENID)
                    {
                        break loop;
                    }
                    else
                    {
                        eq = null;
                    }
                    
                    // If an assignment operator is popped, store it in case
                    // an assignment happens.
                    if (y == t.OPERATORASSIGN)
                    {
                        eq = y;
                    }
                    
                    // If a keyword that starts a line appears, stop popping.
                    if (y == t.KEYWORDPRINT || y == t.KEYWORDGET || y == t.KEYWORDIF || y == t.KEYWORDWHILE || y == t.KEYWORDFOR || y == t.KEYWORDEND)
                    {
                        break loop;
                    }
                }
                while (!isEmpty());
                
                // If the stack isn't empty and not in the middle of a block
                // statement, then a semicolon must be missing somewhere.
                if (!isEmpty())
                {
                    if (s.peek() != t.KEYWORDTHEN && s.peek() != t.KEYWORDDO)
                    {
                        result = false;
                        System.out.println("ERROR: Missing semicolon.");
                        clean();
                    }
                }
                
                if (loop != true)
                {
                    m.interpret(interpretS, interpretL);
                    cleanInterpreter();
                }
                
                break;
            default:
                // If an unknown token is detected, report an error.
                result = false;
                System.out.println("ERROR: Unknown token.");
                clean();
        }
        
        return result;
    }
}
