/*
* Authors: Ben Brewer, Kareem Khalidi, and Joey Mauriello
* File: HuffmanEncoding.java
* Date: 4/11/23
* Purpose: This file is responsible for creating the Huffman Encoding algorithm
*/

import java.io.*;
import java.util.*;

public class HuffmanEncoding
{
   final static String fileName = "RandomText.txt";
   char[] sortedChars;

   public static String parseText(String fileName) throws FileNotFoundException
   {
       String text = "";
       File file = new File(fileName);
       Scanner input = new Scanner(file);
       while (input.hasNextLine())
       {
           text += input.nextLine();
       }
       return text;
   }


   public static HashMap getCharFrequency(String text)
   {
       // create a dictionary of characters and their frequencies
       HashMap<Character, Integer> charFrequency = new HashMap<Character, Integer>();
       for (int i = 0; i < text.length(); i++)
       {
           if (charFrequency.containsKey(text.charAt(i)))
           {
               charFrequency.put(text.charAt(i), charFrequency.get(text.charAt(i)) + 1);
           }
           // char not in dict yet so add it
           else
           {
               charFrequency.put(text.charAt(i), 1);
           }
       }
       return charFrequency;
   }


   public static int getUniqueChars(HashMap<Character, Integer> charFrequency)
   {
       return charFrequency.size();
   }


   // Sort the characters using an array based max priority queue
   // sortChars returns a sorted array of characters
   public char[] sortChars(HashMap<Character, Integer> charFrequency)
   {
       char[] sortedChars = new char[charFrequency.size()];
       HuffmanEncodingTree<Character> maxPQ = new HuffmanEncodingTree<Character>(charFrequency.size());
       // add all the characters to the max priority queue
       for (Character c : charFrequency.keySet())
       {
           maxPQ.insert(new CustomChar(c, charFrequency.get(c)));
       }
       return sortedChars;
   }


   public void tooString()
   {
       System.out.println("[");
       for (int i = 0; i < sortedChars.length; i++)
       {
           System.out.print(", " + sortedChars[i]);
       }
       System.out.print("]");
   }

   // track the binary string value by using recursion to change the encoding value of the CustomChar from the max heap
   public void assignBinaryString(HuffmanEncodingTree<CustomChar> maxPQ, String binaryString) throws EmptyQueueException
   {
       if (maxPQ.isEmpty())
       {
           return;
       }
       else
       {
        CustomChar c = maxPQ.delMax();
        c.setEncoding(binaryString);
        assignBinaryString(maxPQ, binaryString + "0");
        assignBinaryString(maxPQ, binaryString + "1");
       }
   }
}