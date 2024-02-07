/*
 * Prog22.java -- this program takes in a binary file and a binary 
 *                index file name as a command line argument. It
 *                prompts the user to search for a crater name
 *                within the index file which is used to get
 *                the location of that crater in the binary file
 *                by hashing it. The hash returns the bucket that
 *                the crater is in and then a linear search is
 *                performed within that bucket. If the crater is
 *                found the corresponding index is used to find
 *                that crater within the binary file and print
 *                its content.
 *
 * Author:     Ben Brewer
 * Instructor: Lester McCann
 * T/A:        Samantha Cox
 * Course:     CSC 460 Database Design
 *
 * First Version: 02/06/2024
 */

import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

/*+----------------------------------------------------------------------
 ||
 ||  Class Prog22
 ||
 ||         Author:  Ben Brewer
 ||
 ||        Purpose:  searches for a crater within an index file using hashing
 ||
 ||  Inherits From:  None.
 ||
 ||     Interfaces:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  Just the default constructor; no arguments.
 ||
 ||  Class Methods:  int hash(String name, int h)
 ||                  void printHelpfulData(int longestCraterName, int totalBuckets, int h)
 ||                  void printAllRecords(RandomAccessFile indexBinaryFile, int totalBuckets, int totalRecords, int longestCraterName)
 ||                  void searchForCrater(RandomAccessFile indexBinaryFile, RandomAccessFile binFile, int h)
 ||                  void printCraterInfo(RandomAccessFile binFile, int seekOffset)
 ||                  String readString(RandomAccessFile file, int maxLength)
 ||                  double readFixedLengthDouble(RandomAccessFile file)
 ||                  int hashFindCrater(RandomAccessFile indexBinaryFile, String targetName, int h, int longestCraterName)
 ||                  
 ||
 ||  Inst. Methods:  int getTotalBuckets(RandomAccessFile indexBinaryFile, int longestCraterName)
 ||                  int getTotalRecords(RandomAccessFile binaryFile)
 ||
 ++-----------------------------------------------------------------------*/

public class Prog22 {

    /*---------------------------------------------------------------------
        |  Method getTotalBuckets(RandomAccessFile indexBinaryFile, int longestCraterName)
        |
        |  Purpose: getter for amount of buckets in indexBinaryFile
        |
        |  Pre-condition: indexBinaryFile exists
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile indexBinaryFile - file to parse
        |              int longestCraterName - length of longest key string
        |       
        |  Returns: int numberOfBuckets - number of buckets in file
        *-------------------------------------------------------------------*/
    private static int getTotalBuckets(RandomAccessFile indexBinaryFile, int longestCraterName) throws IOException {
        indexBinaryFile.seek(0);
        int recordLength = longestCraterName + 4; // length of a record is the string + an int as the index
        int numberOfBuckets = (int) (((indexBinaryFile.length()) / recordLength) / 25); // number of buckets in index file
        return numberOfBuckets;
    }

    /*---------------------------------------------------------------------
        |  Method getTotalRecords(RandomAccessFile binaryFile)
        |
        |  Purpose: getter for total records in binary file
        |
        |  Pre-condition: binaryFile exists
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binaryFile - file to parse
        |       
        |  Returns: int totalRecords - total records in binary file
        *-------------------------------------------------------------------*/
    private static int getTotalRecords(RandomAccessFile binaryFile) throws IOException {
        binaryFile.seek(0);
        int longestCraterName = binaryFile.readInt(); // length of longest key string
        int longestString2 = binaryFile.readInt(); // length of longest string 2
        double recordLength = longestCraterName + longestString2 + (8*8); // record length is 2 strings and 8 doubles
        int totalRecords = (int) ((binaryFile.length() - 4) / recordLength); // total records in binary file
        return totalRecords;
    }

    /*---------------------------------------------------------------------
        |  Method hash(String name, int h)
        |
        |  Purpose:  used to calculate the hash of a given string
        |            using dynamic value h. Equation was given in spec
        |
        |  Pre-condition: None.
        |
        |  Post-condition: None.
        |
        |  Parameters: String name - string to hash
        |              int h - value used to calculate hash
        |       
        |  Returns: int hash - hash of string
        *-------------------------------------------------------------------*/
    private static int hash(String name, int h) {
        int hash = (int) (Math.abs(name.hashCode() % (Math.pow(2, h+1)))); // equation given in spec
        return hash;
    }

    /*---------------------------------------------------------------------
        |  Method printHelpfulData(int longestCraterName, int totalBuckets, int h)
        |
        |  Purpose:  helper function used for debugging, prints important
        |            data in file
        |
        |  Pre-condition: None.
        |
        |  Post-condition: important file content displayed in terminal
        |
        |  Parameters: int longestCraterName - length of longest key string
        |              int totalBuckets - total number of buckets in index file
        |              int h - current h value used for hashing
        |       
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void printHelpfulData(int longestCraterName, int totalBuckets, int h) {
        System.out.println("helpful data:");
        System.out.println("length longest crater name: " + longestCraterName);
        System.out.println("total buckets: " + totalBuckets);
        System.out.println("h: " + h);
    }

    /*---------------------------------------------------------------------
        |  Method printAllRecords(RandomAccessFile indexBinaryFile, int totalBuckets, int totalRecords, int longestCraterName)
        |
        |  Purpose:  helper function print all records in index file
        |
        |  Pre-condition: indexBinaryFile exists
        |
        |  Post-condition: All records displayed in terminal with their location
        |                  inside the binary file
        |
        |  Parameters: RandomAccessFile indexBinaryFile - index file to parse
        |              int totalBuckets - total buckets in index file
        |              int totalRecords - total number of records in binary file
        |              int longestCraterName - length of longest key string
        |       
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void printAllRecords(RandomAccessFile indexBinaryFile, int totalBuckets, int totalRecords, int longestCraterName) throws IOException {
        indexBinaryFile.seek(0);
        int recordNum = 0; // current record number counter
        for (int i = 0; i < totalBuckets; i++) {
            for (int j = 0; j < 25; j ++) {
                String record = readString(indexBinaryFile, longestCraterName); // current record string
                int index = indexBinaryFile.readInt(); // index of current record
                if (!record.trim().isEmpty()) {
                    recordNum += 1;
                    System.out.println("Record #" + recordNum + ": " + record + " Index: " + index);
                }
            }
        }
    }

    /*---------------------------------------------------------------------
        |  Method searchForCrater(RandomAccessFile indexBinaryFile, RandomAccessFile binFile, int h)
        |
        |  Purpose:  prompts the user for a crater name to search for
        |
        |  Pre-condition: indexBinaryfile and binFile exist
        |
        |  Post-condition: crater info will be printed, else crater not found message
        |
        |  Parameters: RandomAccessFile indexBinaryFile - index file to parse
        |              RandomAccessFile binFile - binary file to parse for crater info
        |              int h - h used to calculate hash
        |       
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void searchForCrater(RandomAccessFile indexBinaryFile, RandomAccessFile binFile, int h) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        int numberOfRecords = (int) ((binFile.length() - 8) / recordLength); // total number of records
        String targetName = ""; // target name to be changed after scanner is initialized and called
        int craterIndex = 0; // initialization of current crater index
        Scanner scanner = new Scanner(System.in); // scanner object to get user input
        while(true){
            System.out.println("Enter crater name (or type 'e' to quit): ");
            targetName = scanner.nextLine().trim();
            System.out.println();
            if (targetName.equalsIgnoreCase("e")) {
                break;
            }
            craterIndex = hashFindCrater(indexBinaryFile, targetName, h, longestString1);
            if (craterIndex == -1) {
                System.out.println("crater \'" + targetName + "\' was not found");
                System.out.println();
            } else {
                System.out.println("Crater found:");
                printCraterInfo(binFile, craterIndex);
                System.out.println();
            }
        }
        scanner.close();
    }

    /*---------------------------------------------------------------------
        |  Method printCraterInfo(RandomAccessFile binFile, int seekOffset)
        |
        |  Purpose:  used to print crater info within binary file
        |
        |  Pre-condition: binFile exists
        |
        |  Post-condition: crater info is displayed in terminal
        |
        |  Parameters: RandomAccessFile binFile - binary file to get crater info from
        |              int seekOffset - location within binary file to start parse from
        |       
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void printCraterInfo(RandomAccessFile binFile, int seekOffset) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        binFile.seek(seekOffset);
        String name = readString(binFile, longestString1); // name of crater
        double diameter = readFixedLengthDouble(binFile); // diameter of crater
        binFile.seek(binFile.getFilePointer() + (4 * 8));
        double depth = readFixedLengthDouble(binFile); // apparent depth of crater
        binFile.seek(binFile.getFilePointer() + (2 * 8));
        String age = readString(binFile, longestString2).trim(); // string age of crater
        if (age == null || age.isEmpty()) {
            age = "null";
        }
        System.out.print("[" + name + "] [" + diameter + "] [" + depth + "] [" + age + "]");

        System.out.println();
    }

    /*---------------------------------------------------------------------
        |  Method readString(RandomAccessFile file, int maxLength)
        |
        |  Purpose:  helper function to convert bytes to a string
        |
        |  Pre-condition: file pointer is at current string location
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile file - file to convert bytes in
        |              int maxLength - length in bytes of string to convert
        |       
        |  Returns: String new String(bytes) - converted string
        *-------------------------------------------------------------------*/
    private static String readString(RandomAccessFile file, int maxLength) throws IOException {
        byte[] bytes = new byte[maxLength]; // creates byte array of correct length
        file.readFully(bytes);
        return new String(bytes); // String to return
    }

    /*---------------------------------------------------------------------
        |  Method readFixedLengthDouble(RandomAccessFile file)
        |
        |  Purpose:  used to convert bytes to a double
        |
        |  Pre-condition: file exists
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile file - file to parse and convert from
        |       
        |  Returns: double ByteBuffer.wrap(bytes).getDouble() - converted double
        *-------------------------------------------------------------------*/
    private static double readFixedLengthDouble(RandomAccessFile file) throws IOException {
        byte[] bytes = new byte[8]; // byte array of preset size of 8 for doubles
        file.readFully(bytes);
        return ByteBuffer.wrap(bytes).getDouble(); // double to be returned
    }

    /*---------------------------------------------------------------------
        |  Method hashFindCrater(RandomAccessFile indexBinaryFile, String targetName, int h, int longestCraterName)
        |
        |  Purpose:  finds the index of the crater by hashing
        |
        |  Pre-condition: indexBinaryFile exists
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile indexBinaryFile - index file to parse
        |              String targetName - target string key to search for
        |              int h - used to calculate hash
        |              int longestCraterName - length of longest key string
        |       
        |  Returns: int currCraterIndex - index of crater in binary file, else -1 if not found
        *-------------------------------------------------------------------*/
    private static int hashFindCrater(RandomAccessFile indexBinaryFile, String targetName, int h, int longestCraterName) throws IOException {
        int bucketIndexOffset = (hash(targetName, h) * 25 * (4+longestCraterName)); // start of bucket index
        indexBinaryFile.seek(bucketIndexOffset);
        for (int i = 0; i < 25; i++) {
            indexBinaryFile.seek(bucketIndexOffset + (i*(longestCraterName+4)));
            String currCraterName = readString(indexBinaryFile, longestCraterName); // current crater in bucket
            int currCraterIndex = indexBinaryFile.readInt(); // current crater index in bucket
            if (currCraterName.trim().equals(targetName.trim())) {
                return currCraterIndex;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String indexBinaryFileName = ""; // name of file to be assigned after reading in command line argument[0]
        String binaryFileName = ""; // name of binary file to be assigned after reading in command line argument[1]
        if (args.length != 2) {
            System.out.println("Incorrect Usage: java Prog22 <filename.idx> <filename.bin>");
            System.exit(1);
        }
        else {
            indexBinaryFileName = args[0];
            binaryFileName = args[1];
        }
        try {
            RandomAccessFile indexBinaryFile = new RandomAccessFile(indexBinaryFileName, "r"); // initialize index file
            int longestCraterName = 0; // intitialize length of longest crater string
            int totalRecords = 0; // counter for total records
            int h = 0; // current h value to be read in
            try {
                RandomAccessFile binaryFile = new RandomAccessFile(binaryFileName, "r"); // initialize binary file
                indexBinaryFile.seek(indexBinaryFile.length() - 4);
                h = indexBinaryFile.readInt();
                longestCraterName = binaryFile.readInt();
                totalRecords = getTotalRecords(binaryFile);
                searchForCrater(indexBinaryFile, binaryFile, h);
                binaryFile.close();
            } catch (IOException e) {
                System.out.println(binaryFileName + " does not exist");
                System.out.println("Incorrect Usage: java Prog22 <filename.idx> <filename.bin>");
            }
            int totalBuckets = getTotalBuckets(indexBinaryFile, longestCraterName); // total buckets in index file
            indexBinaryFile.seek(indexBinaryFile.length() - 4);
            indexBinaryFile.close();
        } catch (IOException e) {
            System.out.println(indexBinaryFileName + " does not exist");
            System.out.println("Incorrect Usage: java Prog22 <filename.idx> <filename.bin>");
        }
    }
}