import java.util.*;
import java.io.*;

public class Decompression
{
    public static void main(String [] args) throws FileNotFoundException, EOFException, IOException
    {
        String fileName = args[0];
        
        File inputFile = new File(fileName);
        
        Scanner keyboard = new Scanner(System.in);
        
        while(!inputFile.exists())
        {
            System.out.print("File does not exist, please enter again: ");
            
            fileName = keyboard.nextLine();
            
            inputFile = new File(fileName);
        }
        
        InputStream fileStream = null;
        
        BufferedInputStream bufferedStream = null;
        
        String compressedFileName = fileName + ".zzz";
        
        fileStream = new FileInputStream(compressedFileName);
                
        bufferedStream = new BufferedInputStream(fileStream);
        
        decompressFile(bufferedStream, fileName);
    }
    
    public static LinkedHashTable initializeDictionary()
    {
        int asciiValue;
        LinkedHashTable dictionary = new LinkedHashTable(193);
        
        for(int i = 0; i < 96; i++)
        {
            asciiValue = 32 + i;
            dictionary.add(i, -1, (char) asciiValue);
        }
        
        return dictionary;
    }
    
    public static String firstChar(String prefix)
    {
        return Character.toString(prefix.charAt(0));
    }
    
    public static void decompressFile(BufferedInputStream compressedFileStream, String decompressedFileName) throws EOFException, IOException
    {
        LinkedHashTable dictionary = initializeDictionary();
        
        DataInputStream compressedFile = null;
        
        int p, q;
    
        int previousCharIndex;
        
        String newPrefix, emptyString = "", word;
        
        int decompressedWordsIndex = 0;
        
        int nextEmptyDicIndex = 96;
        
        compressedFile = new DataInputStream(compressedFileStream);
        
        ArrayList<Integer> compressedWords = new ArrayList<Integer>();
        
        ArrayList<String> decompressedWords = new ArrayList<String>();
        
        boolean run = true;
        
        int temp;
        
        while(run)
        {
            try
            {
                temp = compressedFile.readInt();
            }
            catch(EOFException exception)
            {
                break;
            }
            
            compressedWords.add(temp);
        }
        
        word = emptyString;
        
        p = compressedWords.get(0);
        
        word += dictionary.prefixAt(p, 0);
        
        for(int index = 1; index < compressedWords.size(); index++)
        {
            previousCharIndex = index - 1;
            
            p = compressedWords.get(index);
            
            q = compressedWords.get(previousCharIndex);
            
            //System.out.println(q);
            
            //System.out.println(p);
            
            if(!dictionary.listAt(p).isEmpty())
            {
                word += dictionary.prefixAt(p, 0);
                
                if(p != 0)
                {
                    if(q != 0)
                    {
                        newPrefix = dictionary.prefixAt(q, 0) + firstChar(dictionary.prefixAt(p, 0));
                        
                        //newPrefix.trim();
                        
                        dictionary.add(nextEmptyDicIndex, newPrefix);
                        
                        nextEmptyDicIndex++;
                    }
                    
                }
                else
                {
                    decompressedWords.add(word);
                    
                    word = emptyString;
                }
            }
            else
            {
                newPrefix = dictionary.prefixAt(q, 0) + firstChar(dictionary.prefixAt(q, 0));
                
                word += newPrefix;
                
                dictionary.add(nextEmptyDicIndex, newPrefix);
            }
        }
        
        writeToFile(decompressedWords, decompressedFileName);
        
    }
    
    private static void writeToFile(ArrayList<String> decodedWords, String fileName) throws FileNotFoundException
    {
        
        String outputFileName = fileName + ".log";
        
        PrintWriter out = new PrintWriter(outputFileName);

        for(int i = 0; i < decodedWords.size(); i++)
        {
            out.print(decodedWords.get(i));
        }
        
        out.close();
        
    }
    
    
}
