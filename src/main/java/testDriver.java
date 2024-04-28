// Import the Scanner, ArrayList, and File
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
public class testDriver {
    public static void main(String[] args) throws Exception
    {
        // Open the file input.txt.
        File f = new File("input.txt");
        // Set the Scanner to read from the file.
        Scanner input = new Scanner(f);
        Parser p = new Parser();
        boolean x = true;
        
        while (input.hasNext())
        {
            // While end of file hasn't been reached, store a line from the file.
            String line = input.nextLine();
            
            // Invoke the lexer.
            Lexer l = new Lexer();
            // Get an arraylist of words from the line using the lexer.
            ArrayList<Word> words = l.lex(line);
        
            for (Word word : words)
            {
                // For every word, parse it.
                x = p.parse(word);
                
                // If there's an error, stop.
                if (!x)
                {
                    break;
                }
                
                // If there's no error, print out the word.
                //System.out.println(word);
            }
            
            // If there's an error, stop reading.
            if (!x)
            {
                break;
            }
        }
        
        // If the stack isn't empty, report an error and clean it.
        if (!p.isEmpty())
        {
            System.out.println("ERROR: Stack has not been emptied.");
            x = false;
            p.clean();
        }
        
        // If there are no errors, then the program is legal.
        if (x)
        {
            //System.out.println("Program is legal.");
        }
    }
}
