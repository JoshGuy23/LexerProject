// Author: Joshua Hecker
//import java.util.LinkedList;
public class HashTable {
    private HashNode[] node = new HashNode[13];
    // Methods to implement: add, remove, search, retrieve
    HashTable()
    {
        for (int i = 0; i < 13; i++)
        {
            node[i] = new HashNode();
            node[i].key = i;
            node[i].id = "";
        }
    }
    
    private static int getKey(String i)
    {
        int k = 0;
        
        for (int j = 0; j < i.length(); j++)
        {
            k += (int)i.charAt(j);
        }
        
        k = ((12 * k + 1) % 17) % 13;
        
        return k;
    }
    
    public void add(String f, int v)
    {
        int k = getKey(f);
        HashNode n;
        HashNode s = node[k];
        
        while (s.next != null)
        {
            if (s.id.equals(f))
            {
                s.value = v;
                return;
            }
            else
            {
                s = s.next;
            }
        }
        
        if (s.id.equals(f))
        {
            s.value = v;
            return;
        }
        
        n = new HashNode();
        n.id = f;
        n.value = v;
        n.key = k;
        
        s.next = n;
    }
    
    public boolean search(String f)
    {
        boolean found = false;
        int k = getKey(f);
        HashNode s = node[k];
        
        while (s.next != null)
        {
            if (s.id.equals(f))
            {
                found = true;
                break;
            }
            
            s = s.next;
        }
        
        if (!found && s.id.equals(f))
        {
            found = true;
        }
        
        return found;
    }
    
    public HashNode retrieve(String f)
    {
        int k = getKey(f);
        HashNode s = node[k];
        
        while (s.next != null)
        {
            if (s.id.equals(f))
            {
                return s;
            }
            
            s = s.next;
        }
        
        if (s.id.equals(f))
        {
            return s;
        }
        
        return null;
    }
    
    public void clear()
    {
        for (int i = 0; i < 13; i++)
        {
            node[i].next = null;
        }
    }
}
