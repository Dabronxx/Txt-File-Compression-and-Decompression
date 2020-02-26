import java.util.*;
import java.io.*;

public class LinkedHashTable
{
    private int tableSize;
    
    private MyLinkedList[] dictionary = null;
    
    private int redoubles;
    
    public LinkedHashTable(int tableSize)
    {
        redoubles = 0;
        this.tableSize = tableSize;
        dictionary = initializeList(tableSize);
    }
    
    private MyLinkedList[] initializeList(int tableSize)
    {
        MyLinkedList[] list = new MyLinkedList[tableSize];
        for(int i = 0; i < tableSize; i++)
        {
            list[i] = new MyLinkedList();
        }
        
        return list;
    }
    
    public void add(int index, String prefix)
    {
        short prefixShort = getPrefixShort(prefix);
        char lastChar = prefix.charAt(prefix.length() - 1);
        
        add(index, prefixShort, lastChar);
    }
    
    public void add(int index, short numberPrefix, char singleLetter)
    {
        dictionary[index].add(numberPrefix, singleLetter);
        
        if(capacity() >= .75)
        {
            rehash();
            redoubles++;
        }
    }
    
    public double loadFactor()
    {
        int totalItems = 0;
        
        for(int i = 0; i < tableSize; i++)
        {
            totalItems += dictionary[i].size();
        }
        
        return totalItems / (double) tableSize;
    }
    
    public double capacity()
    {
        int filledSpots = 0;
        
        for(int i = 0; i < tableSize; i++)
        {
            if(!dictionary[i].isEmpty())
            {
                filledSpots++;
            }
        }
        return filledSpots / (double) tableSize;
    }
    
    private int longestList()
    {
        int longest = 0;
        
        for(int i = 0; i < tableSize; i++)
        {
            if(dictionary[i].size() > longest)
            {
                longest = dictionary[i].size();
            }
        }
        
        return longest;
    }
    
    private boolean isPrime(int num)
    {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) return false;
        return true;
    }
    
    private void rehash()
    {
        int newTableSize = tableSize * 2 + 1;
        
        while(!isPrime(newTableSize))
        {
            newTableSize++;
        }
        
        MyLinkedList[] temp = null;
        
        temp = initializeList(tableSize);
        
        for(int i = 0; i < tableSize; i++)
        {
            temp[i] = dictionary[i].clone();
        }
        
        dictionary = initializeList(newTableSize);
        
        for(int i = 0; i < tableSize; i++)
        {
            dictionary[i] = temp[i].clone();
        }
        
        tableSize = newTableSize;
    }
    
    private String convertToString(short numberPrefix, char singleLetter)
    {
        if(numberPrefix == -1)
        {
            return Character.toString(singleLetter);
        }
        else
        {
            short numberPrefixShort = dictionary[numberPrefix].getShort(0);
            char numberPrefixChar = dictionary[numberPrefix].getChar(0);
            return convertToString(numberPrefixShort, numberPrefixChar) + Character.toString(singleLetter);
        }
    }
    
    public String prefixStringAt(int index)
    {
        return convertToString(dictionary[index].getShort(0), dictionary[index].getChar(0));
    }
    
    public int indexOf(String prefix)
    {
        short numberPrefix = getPrefixShort(prefix);
        char lastChar = prefix.charAt(prefix.length() - 1);
        return indexOf(numberPrefix, lastChar);
    }
    
    public int indexOf(short numberPrefix, char singleLetter)
    {
        int prefixSpot = 0;
        
        short prefixShort = 0;
        
        char lastLetter;
        
        String prefix = convertToString(numberPrefix, singleLetter);
        
        char firstLetter = prefix.charAt(prefixSpot);
        
        String partialPrefix = Character.toString(firstLetter);
        
        MyLinkedList list = new MyLinkedList();
        
        int index = -1;
        
        short singleCharShort = -1;
        
        for(int i = 0; i < 96; i++)
        {
            if(dictionary[i].contains(singleCharShort, firstLetter))
            {
                index = i;
                break;
            }
        }
        
        if(prefix.length() == 1 || index == -1)
        {
            return index;
        }
        else
        {
            while(index != -1 && !partialPrefix.equals(prefix))
            {
                list = dictionary[index];
                
                prefixSpot++;
                
                lastLetter = prefix.charAt(prefixSpot);
                
                partialPrefix += Character.toString(lastLetter);
                
                prefixShort = getPrefixShort(partialPrefix);
                
                index = prefixIndex(prefixShort, lastLetter, list);
            }
        }
        return index;
    }
    
    public boolean contains(String prefix)
    {
        
    }
    
    public boolean contains(short numberPrefix, char singleLetter)
    {
        return indexOf(numberPrefix, singleLetter) != -1;
    }
    
    public boolean isLongest(String prefix, String nextPrefix)
    {
        short firstShort = getPrefixShort(prefix);
        char firstChar = prefix.charAt(prefix.length() - 1);
        
        short secondShort = getPrefixShort(nextPrefix);
        char secondChar = nextPrefix.charAt(nextPrefix.length() - 1);
        
        return isLongest(firstShort, firstChar, secondShort, secondChar);
        
    }
    
    public boolean isLongest(short firstShort, char firstChar, short secondShort, char secondChar)
    {
        int index = indexOf(firstShort, firstChar);
        if(firstShort == secondShort && firstChar == secondChar)
        {
            return true;
        }
        else
        {
            return !hasPrefix(secondShort, secondChar, dictionary[index]);
        }
    }
    
    
    
    public MyLinkedList listAt(int index)
    {
    	return dictionary[index];
    }
    
    private boolean hasPrefix(short numberPrefix, char singleChar, MyLinkedList tableSlot)
    {
        if(tableSlot.size() == 1)
        {
            return false;
        }
        else if(prefixIndex(numberPrefix, singleChar, tableSlot) != -1)
        {
            return true;
        }
        return false;
    }
    private short prefixIndex(short numberPrefix, char singleChar, MyLinkedList tableSlot)
    {
        short prefixPosition = -1, index;
        
        if(tableSlot.size() == 1)
        {
            return prefixPosition;
        }
        else
        {
            for(int i = 1; i < tableSlot.size(); i++)
            {
                index = tableSlot.getShort(i);
                
                if(dictionary[index].contains(numberPrefix, singleChar))
                {
                    prefixPosition = index;
                    break;
                }
            }
        }
        return prefixPosition;
    }
    
    private short getPrefixShort(String prefix)
    {
        for(int i = 0; i < tableSize; i++)
        {
            if(prefixStringAt(i).equals(prefix))
            {
                return dictionary[i].getShort(0);
            }
        }
        return -2;
    }
    public int totalRehashes()
    {
        return redoubles;
    }
    
    public void printData(File inputFile, String outputFileName, double runTime) throws FileNotFoundException
    {
        String logFileName = outputFileName + ".log";
        
        PrintWriter out = new PrintWriter(logFileName);
        
        long initialFileSize = inputFile.length() / 1000;
        
        String inputFileName = inputFile.getName();
        
        File outputFile = new File(outputFileName);
        
        long outputFileSize = outputFile.length() / 1000;
        
        out.println("Compression of " + inputFileName);
        
        out.print("Compressed from " + initialFileSize + " Kilobytes ");
        
        out.println("to " + outputFileSize + " Kilobytes");
        
        out.println("Compression took " + runTime + " seconds");
        
        out.println("Hash table is " + (capacity() * 100) + "% full");
        
        out.println("The average linked list is " + loadFactor() + "elements long");
        
        out.println("The longest linked list contains " + longestList() + " elements");
        
        out.println("The dictionary contains " + (capacity() * tableSize) + " total entries");
        
        out.println("The table was rehashed " + redoubles + " times");
        
        out.close();
        
    }
    
    
    
    
    
}
