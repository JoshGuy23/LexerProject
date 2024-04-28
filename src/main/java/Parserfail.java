import java.util.Scanner;
import java.io.File;
public class Parserfail {
    private Token T;
    private HashTable table = new HashTable();
    private File f = new File("input.txt");
    private Scanner input = new Scanner(System.in);
    private int line = 1;
    
    public void start() throws Exception
    {
        Scanner inFile = new Scanner(f);
        while (inFile.hasNext())
        {
            String str = inFile.nextLine();
            process(inFile, str, false);
            line++;
        }
    }
    
    private void process(Scanner s, String l, boolean block)
    {
        String check = "";
        int a = 5;
        
        for (int i = 0; i < a; i++)
        {
            check += l.charAt(i);
            
            switch (check)
            {
                case "\t":
                case " ":
                    check = "";
                    a++;
                    break;
                case "do":
                case "get":
                case "if":
                case "then":
                case "else":
                case "for":
                case "":
                case "\n":
                    i = 5;
                    break;
            }
        }
        
        switch (check)
        {
            case "print":
                print(l);
                break;
            case "get":
                get(l);
                break;
        }
    }
    
    private void print(String l)
    {
        if (l.contains(";"))
        {
            if (l.length() > 6)
            {
                if (l.charAt(6) == '\"')
                {
                    if (l.charAt(l.length() - 2) == '\"')
                    {
                        String s = l.substring(7, l.length() - 2);
                        System.out.println(s);
                    }
                    else
                    {
                        System.out.println("Line " + line + ": String not closed.");
                        System.exit(0);
                    }
                }
                
                String s = l.substring(6, l.length() - 1);
                
                if (isDigit(s))
                {
                    System.out.println(s);
                }
                else
                {
                    //Evaluate expression
                }
            }
        }
    }
    
    private void get(String l)
    {
        if (l.charAt(l.length() - 1) == ';')
        {
            if (l.length() > 4)
            {
                String a = l.substring(4, l.length() - 1);
                if (a.contains(" "))
                {
                    int x = input.nextInt();
                    table.add(a, x);
                }
                else
                {
                    System.out.println("Line " + line + ": Invalid identifier.");
                    System.exit(0);
                }
            }
            else
            {
                System.out.println("Line " + line + ": No argument.");
                System.exit(0);
            }
        }
        else
        {
            System.out.println("Line " + line + ": No semicolon.");
            System.exit(0);
        }
    }
    
    private int evaluate(String l)
    {
        l = l.replace(" ", "");
        String buffer = "";
        // Read string into buffer until operator is encountered
        // Operators include <, >, == and variants, plus AND, OR, & NOT.
        // Create additional class for stack operations, probably.
        for (int i = 0; i < l.length(); i++)
        {
            switch (l.charAt(i))
            {
                
            }
        }
        
        //placeholder
        return -1;
    }
    
    private boolean isDigit(String l)
    {
        boolean c = true;
        
        for (int i = 0; i < l.length(); i++)
        {
            char a = l.charAt(i);
            if (!Character.isDigit(i))
            {
                c = false;
                break;
            }
        }
        
        return c;
    }
}
