/* Name: MyLinkedList
 * Authors: Mohammed and Branko
 * Date: Nov. 17, 2017
 * Description: This program contains a custom class, MyLinkedList,
 *                 that simulates what a linked list does, e.g.
 *                 splitting into halves, sublist-ing, removing by
 *                 value, adding a new value, etc.
 */


public class MyLinkedList
{
    // the Node class is a private inner class used (only) by the LinkedList class
    private class Node
    {
        private short num;
        private char letter;
        private Node next;
        
        public Node(short number, char l, Node n)
        {
            num = number;
            letter = l;
            next = n;
        }
        
    }
    
    private Node first;
    private int length;  // to enable an O(1) size method
    
    
    public MyLinkedList() {
        first = null;
        length = 0;  // added after considering the size() method
    }
    
    public boolean isEmpty() {
        return (first == null);
    }
    
    public void addFirst(short c, char d)
    {
        /* These two lines can be reduced to the single line which follows
         *   Node temp = first;
         *   first = new Node(d,temp);
         */
        first = new Node(c, d, first);
        length++;
    }
    
    public int size()
    {
        /*  This O(n) loop can be replaced by the O(1) return once we have the length field
         int count = 0;
         for (Node curr = first; curr != null; curr = curr.next)
         count++;
         return count;
         */
        return length;
    }
    
    public void clear()
    {
        first = null;
        length = 0;
    }
    
    public boolean contains(short number, char lastLetter)
    {
        for (Node curr = first; curr != null; curr = curr.next)
        {
            if (number == curr.num && lastLetter == curr.letter)
            {
                // this implies that the data must have an overridden .equals() method!
                return true;
            }
        }
        return false;
    }
    
    public short getShort(int index)
    {
        if (index < 0 || index >= length)
        {
            return -2;
        }
        
        Node curr = first;
        for (int i = 0; i < index; i++)
            curr = curr.next;
        return curr.num;
    }
    
    public char getChar(int index)
    {
        
        Node curr = first;
        for (int i = 0; i < index; i++)
            curr = curr.next;
        return curr.letter;
    }
    
    public String toString()
    {
        StringBuilder result = new StringBuilder();  //String result = "";
        
        for (Node curr = first; curr != null; curr = curr.next)
            result.append(curr.num + " " + curr.letter + "->");  //result = result + curr.data + "->";
        
        result.append("[null]");
        return result.toString();   //return result + "[null]";
    }
    
    // ------------------------  HW4 methods start here ------------------------
    
    private Node firstNode()
    {
        return first;
    }
    
    public void add(short number, char lastChar)
    {
        
        if (isEmpty())
        {
            first = new Node(number, lastChar, null);
            
        }
        else
        {
            Node last = first;
            
            while (last.next != null) {
                last = last.next;
            }
            
            last.next = new Node(number, lastChar, null);
        }
        
        length++;
    }
    
    
    public void set(int index, short number, char lastLetter)
    {
        
        
        Node toReplace = first;
        
        // Loop until the Node at index - 1 is reached.
        for (int i=0; i < index - 1; i++) {
            toReplace = toReplace.next;
        }
        
       
        
        // Replace the original's next with a new Node
        // which its next  is the original's next next.
        toReplace.next = new Node(number, lastLetter, toReplace.next.next);
    }
    
    
    
    public MyLinkedList clone()
    {
        
        MyLinkedList result = new MyLinkedList();
        
        if (!isEmpty())
        {
            
            // If the list isn't empty,
            // then we surely have a
            // "first" node, so we can
            // safely always add it here.
            Node current = first;
            result.add(current.num, current.letter);
            
            while (current.next != null)
            {
                
                // As long as we didn't reach
                // the last node, keep adding
                // the nodes' data one by one
                result.add(current.next.num, current.next.letter);
                
                current = current.next;
            }
        }
        
        return result;
    }
    
    /*public void removeAll(short number, char lastChar)
    {
        if (first.num == number && first.letter == lastChar)
        {
            first = first.next;
        }
        
        Node previous = first;
        
        while (previous.next != null) {
            
            if (previous.next.num == number && previous.next.letter == lastChar)
            {
                // When we find  the value  we are
                // looking for, we set the next of
                // the Node before it  to the Node
                // after it so that the list loses
                // reference to it.
                previous.next = previous.next.next;
                
            }
        }
        
        length = 0;
        
    }*/
    
    /*public boolean equals(Object a, Object b)
    {
        if(a == b)
        {
            return true;
        }
        return false;
    }*/
    
    
    
}

