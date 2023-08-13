/*
* Authors: Ben Brewer, Kareem Khalidi, and Joey Mauriello
* File: HuffmanEncodingTest.java
* Date: 4/11/23
* Purpose: This file is responsible for testing the Huffman Encoding
* algorithm in a real world scenario
*/

import java.io.*;
import java.util.*;
public class HuffmanEncodingTest
{
   final static String fileName = "/Users/brew/Desktop/CSC345/Huffman Encoding Project/RandomText.txt";
   public static void main(String[] args) throws FileNotFoundException, EmptyQueueException {
       /*String text = HuffmanEncoding.parseText(fileName);
       HashMap<Character, Integer> charFrequency = HuffmanEncoding.getCharFrequency(text);


       int numUniqueChars = HuffmanEncoding.getUniqueChars(charFrequency);
       // multiply by 8 to get the number of bits needed to represent the text
       int numBitsNeeded = numUniqueChars * 8;


       System.out.println("Number of bits needed BEFORE encoding: " + numBitsNeeded);


       // call the HuffmanEncoding on the parsed text
       testHuffmanEncoding(charFrequency);*/

       String text = HuffmanEncoding.parseText(fileName);
       HashMap<Character, Integer> charFrequency = HuffmanEncoding.getCharFrequency(text);
       HuffmanEncodingTree test = new HuffmanEncodingTree(charFrequency.size());
       for (Map.Entry<Character, Integer> entry : charFrequency.entrySet())
       {
           test.insert(new CustomChar(entry.getKey(), entry.getValue()));
       }
       test.insert(new CustomChar('\0', Integer.MAX_VALUE));
       while (!test.isEmpty())
       {
           System.out.println(test.delMax().toString());
       }

   }


   public static void testHuffmanEncoding(HashMap<Character, Integer> charFrequency)
   {
       // HuffmanEncoding(charFrequency);
       // TODO
   }


}


