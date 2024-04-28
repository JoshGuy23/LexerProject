import java.util.Stack;
import java.util.Scanner;
public class Interpreter {
    private Scanner in = new Scanner(System.in);
    private boolean condition;
    private Token t;
    private HashTable h = new HashTable();
    private boolean loopcondition;
    private boolean loop;
    
    public Interpreter()
    {
        condition = true;
        loopcondition = true;
        loop = false;
    }
    
    public void reset()
    {
        condition = true;
        loopcondition = true;
    }
    
    public void interpret(Stack<Token> s, Stack<String> l)
    {
        Stack<String> temp = new Stack<>();
        Stack<String> fin = new Stack<>();
        
        while (!s.empty())
        {
            switch (s.pop())
            {
                case KEYWORDIF:
                    l.pop();
                    if (condition == false || loopcondition == false)
                    {
                        return;
                    }
                    
                    while (s.peek() != t.KEYWORDTHEN)
                    {
                        s.pop();
                        temp.push(l.pop());
                    }
                    
                    while (!temp.empty())
                    {
                        fin.push(temp.pop());
                    }
                    
                    if (evaluate(fin) == 0)
                    {
                        condition = false;
                    }
                    
                    resetStack(fin);
                    
                    break;
                case KEYWORDELSE:
                    l.pop();
                    if (condition == true)
                    {
                        return;
                    }
                    
                    break;
                case KEYWORDTHEN:
                case KEYWORDDO:
                    l.pop();
                    break;
                case KEYWORDWHILE:
                    l.pop();
                    if (condition == false || loopcondition == false)
                    {
                        return;
                    }
                    
                    while (s.peek() != t.KEYWORDDO)
                    {
                        s.pop();
                        temp.push(l.pop());
                    }
                    
                    while (!temp.empty())
                    {
                        fin.push(temp.pop());
                    }
                    
                    loop = true;
                    
                    resetStack(fin);
                case KEYWORDPRINT:
                    l.pop();
                    if (condition == false || loopcondition == false)
                    {
                        return;
                    }
                    
                    switch(s.peek())
                    {
                        case TOKENSTRING:
                            s.pop();
                            System.out.print(l.pop());
                            break;
                        case TOKENINT:
                        case TOKENID:
                            System.out.println(evaluate(l));
                            resetStack(s);
                            break;
                        default:
                            System.out.println("ERROR: Unexpected token.");
                            return;
                    }
                    break;
                case KEYWORDGET:
                    l.pop();
                    if (condition == false || loopcondition == false)
                    {
                        return;
                    }
                    h.add(l.pop(), in.nextInt());
                    s.pop();
                    break;
                case TOKENID:
                    if (condition == false || loopcondition == false)
                    {
                        return;
                    }
                    
                    String x = l.pop();
                    String e = l.pop();
                    s.pop();
                    if (!e.equals("="))
                    {
                        return;
                    }
                    
                    h.add(x, evaluate(l));
                    resetStack(s);
                    break;
                default:
                    System.out.println("ERROR: Unexpected token.");
                    return;
            }
        }
    }
    
    public int evaluate(Stack<String> l)
    {
        Stack<Integer> values = new Stack<>();
        Stack<String> operators = new Stack<>();
        int x;
        int y;
        String op;
        String o;
        
        while (!l.empty())
        {
            String s = l.peek();
            
            if (numString(s))
            {
                values.push(Integer.parseInt(l.pop()));
                continue;
            }
            
            if (noSymbols(s) && (!s.equals("and") && !s.equals("or") && !s.equals("not")))
            {
                if (h.search(s))
                {
                    HashNode node = h.retrieve(l.pop());
                    values.push(node.value);
                }
                else
                {
                    System.out.println("ERROR: Variable not found.");
                    System.exit(0);
                }
                
                continue;
            }
            
            switch (s)
            {
                case "(":
                    operators.push(l.pop());
                    break;
                case ")":
                    l.pop();
                    if (operators.search("(") == -1)
                    {
                        System.out.println("ERROR: Incorrect ordering.");
                        System.exit(0);
                    }
                    o = operators.peek();
                    
                    while (!o.equals("("))
                    {
                        op = operators.pop();
                        o = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            x = values.pop();
                            if (x > 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                            continue;
                        }
                        y = values.pop();
                        x = values.pop();
                        
                        values.push(result(x, y, o));
                    }
                    operators.pop();
                    break;
                case "not":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
                case "*":
                case "/":
                case "%":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else if (op.equals("*") || op.equals("/") || op.equals("%"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
                case "+":
                case "-":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else if (op.equals("*") || op.equals("/") || op.equals("%") || op.equals("+") || op.equals("-"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
                case "<":
                case "<=":
                case ">":
                case ">=":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else if (op.equals("*") || op.equals("/") || op.equals("%") || op.equals("+") || op.equals("-"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else if (op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">="))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
                case "==":
                case "!=":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else if (op.equals("*") || op.equals("/") || op.equals("%") || op.equals("+") || op.equals("-"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else if (op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=") || op.equals("==") || op.equals("!="))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
                case "and":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else if (op.equals("*") || op.equals("/") || op.equals("%") || op.equals("+") || op.equals("-"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else if (op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=") || op.equals("==") || op.equals("!="))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else if (op.equals("and"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
                case "or":
                    while (!operators.empty())
                    {
                        op = operators.peek();
                        
                        if (op.equals("not"))
                        {
                            operators.pop();
                            x = values.pop();
                            
                            if (x != 0)
                            {
                                values.push(0);
                            }
                            else
                            {
                                values.push(1);
                            }
                        }
                        else if (op.equals("*") || op.equals("/") || op.equals("%") || op.equals("+") || op.equals("-"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else if (op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=") || op.equals("==") || op.equals("!="))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else if (op.equals("and") || op.equals("or"))
                        {
                            y = values.pop();
                            x = values.pop();
                            o = operators.pop();
                            values.push(result(x, y, o));
                        }
                        else
                        {
                            break;
                        }
                    }
                    operators.push(l.pop());
                    break;
            }
        }
        
        while (!operators.empty())
        {
            o = operators.pop();
            
            if (o.equals("not"))
            {
                x = values.pop();
                
                if (x != 0)
                {
                    values.push(0);
                }
                else
                {
                    values.push(1);
                }
                
                continue;
            }
            
            y = values.pop();
            x = values.pop();
            values.push(result(x, y, o));
        }
        
        return values.pop();
    }
    
    private int result(int x, int y, String o)
    {
        int r = -9999;
        
        switch(o)
        {
            case "+":
                r = x + y;
                break;
            case "-":
                r = x - y;
                break;
            case "*":
                r = x * y;
                break;
            case "/":
                r = x / y;
                break;
            case "%":
                r = x % y;
                break;
            case "<":
                if (x < y)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
            case "<=":
                if (x <= y)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
            case ">":
                if (x > y)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
            case ">=":
                if (x >= y)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
            case "==":
                if (x == y)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
            case "!=":
                if (x != y)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
            case "and":
                if (x == 0 || y == 0)
                {
                    r = 0;
                }
                else
                {
                    r = 1;
                }
                break;
            case "or":
                if (x > 0 || y > 0)
                {
                    r = 1;
                }
                else
                {
                    r = 0;
                }
                break;
        }
        
        return r;
    }
    
    private boolean numString(String s)
    {
        if (s == null)
        {
            return false;
        }
        
        try
        {
            int i = Integer.parseInt(s);
        }
        catch(NumberFormatException n)
        {
            return false;
        }
        
        return true;
    }
    
    private boolean noSymbols(String s)
    {
        if (s == null)
        {
            return false;
        }
        
        for (int i = 0; i < s.length(); i++)
        {
            switch (s.charAt(i))
            {
                case '+':
                case '-':
                case '*':
                case '/':
                case '%':
                case '>':
                case '<':
                case '=':
                case '!':
                case '(':
                case ')':
                    return false;
            }
        }
        
        return true;
    }
    
    private void resetStack(Stack s)
    {
        while (!s.empty())
        {
            s.pop();
        }
    }
}
