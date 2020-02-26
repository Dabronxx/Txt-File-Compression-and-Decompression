import java.util.*;
import java.io.*;

public class Compression
{
    public static void main(String [] args) throws FileNotFoundException, IOException
    {
        Scanner keyboard = new Scanner(System.in);
        
        String inputFileName = args[0];
        
        String outputFileName = inputFileName + ".zzz";
        
        File inputFile = new File(inputFileName);
        
        while(!inputFile.exists())
        {
            System.out.print("File does not exist, please enter again: ");
            
            inputFileName = keyboard.nextLine();
            
            inputFile = new File(inputFileName);
        }
        
        compressFile(inputFile, outputFileName);
    }
    
    public static LinkedHashTable initializeDictionary()
    {
        int asciiValue;
        LinkedHashTable dictionary = new LinkedHashTable(193);
        
        for(int i = 0; i < 96; i++)
        {
            asciiValue = 32 + i;
            dictionary.add(i, (short) -1, (char) asciiValue);
        }
        
        return dictionary;
    }
    
    public static void compressFile(File inputFile, String outputFileName) throws FileNotFoundException, IOException
    {
        long startTime = System.nanoTime();
        
        long endTime;
        
        double totalTime;
        
        LinkedHashTable dictionary = initializeDictionary();
        
        int nextEmptyIndex = 96;
        
        int nextCharIndex = 0;
        
        int longestIndex;
        
        String word, currentPrefix, nextChar, nextPrefix, logFileName;
        
        MyLinkedList currentList = null;
        
        ArrayList<Integer> compressedWords = new ArrayList<Integer>();
        
        Scanner fileReader = new Scanner(inputFile);
        
        StringTokenizer fileTokenizer = null;
        
        while(fileReader.hasNext())
        {
            fileTokenizer = new StringTokenizer(fileReader.nextLine());
            
            while(fileTokenizer.hasMoreTokens())
            {
                word = fileTokenizer.nextToken();
                
                currentPrefix = "";
                
                for(int charIndex = 0; charIndex < word.length(); charIndex++)
                {
                    currentPrefix += Character.toString(word.charAt(charIndex));
                    
                    //System.out.println("current prefix = " + currentPrefix);
                    
                    currentList = dictionary.listAt(dictionary.indexOf(currentPrefix));
                    
                    if(charIndex == word.length() - 1)
                    {
                        nextChar = "";
                    }
                    else
                    {
                        nextCharIndex = charIndex + 1;
                        
                        nextChar = Character.toString(word.charAt(nextCharIndex));
                    }
                    
                    nextPrefix = currentPrefix + nextChar;
                    
                    System.out.println(nextPrefix);
                    while(!dictionary.isLongest(currentPrefix, nextPrefix))
                    {
                        charIndex = nextCharIndex;
                        
                        currentPrefix = nextPrefix;
                        
                        nextCharIndex++;
                        
                        currentList = dictionary.listAt(dictionary.indexOf(currentPrefix));
                        
                        if(nextCharIndex ==  word.length())
                        {
                            break;
                        }
                        else
                        {
                            nextChar = Character.toString(word.charAt(nextCharIndex));
                            nextPrefix = currentPrefix + nextChar;
                        }
                    }
                    longestIndex = dictionary.indexOf(currentPrefix);
                    
                    compressedWords.add(longestIndex);
                    
                    if(!currentPrefix.equals(nextPrefix))
                    {
                        //System.out.println("Next Prefix = " + nextPrefix);
                        //System.out.println("NextEmptyIndex = " + nextEmptyIndex);
                        dictionary.add(nextEmptyIndex, nextPrefix);
                    }
                    
                    currentList.add((short)nextEmptyIndex, Character.MIN_VALUE);
                    
                    currentPrefix = "";
                    
                    if(!dictionary.listAt(nextEmptyIndex).isEmpty())
                    {
                        nextEmptyIndex++;
                    }
                    
                }
                compressedWords.add(0);
            }
            
            /*for(int index = 0; index < compressedWords.size(); index++)
            {
                System.out.println(compressedWords.get(index));
            }*/
            
            
        }
        
        fileReader.close();
        
        writeToFile(compressedWords, outputFileName);
        
        endTime = System.nanoTime();
        
        totalTime = (endTime - startTime ) / 100000000.0;
        
        dictionary.printData(inputFile, outputFileName, totalTime);
    
    }
    
    
    
    private static void writeToFile(ArrayList<Integer> codedWords, String outputFileName) throws FileNotFoundException, IOException
    {
        DataOutputStream out = null;
        try
        {
            out = new DataOutputStream(new FileOutputStream(outputFileName));
        }
        catch(FileNotFoundException ex)
        {
            
        }
        
        for(int i = 0; i < codedWords.size(); i++)
        {
            out.writeInt(codedWords.get(i));
        }
                                                    
    }
    
    private static void write2File(ArrayList<Integer> codedWords, String fileName) throws FileNotFoundException
    {
        PrintWriter out = new PrintWriter(fileName);
        
        for(int i = 0; i < codedWords.size(); i++)
        {
            out.print(codedWords.get(i) + " ");
        }
        
        out.close();
    }
}
