
/*
 * Prog1B.java -- this program takes in a bin file name as a command
 *                line argument and reads in bytes for a specific 
 *                operation such as getting and printing: the first
 *                three craters in the bin file, middle 3 or 4, 
 *                last three, and top ten based on apparent depth.
 *                It then displays the total number of records and
 *                allows the user to search for the a crater by it's
 *                name and it will output data corresponding to that 
 *                crater.
 *
 * Author:     Ben Brewer
 * Instructor: Lester McCann
 * T/A:        Samantha Cox
 * Course:     CSC 460 Database Design
 *
 * First Version: 01/22/2024
 */

import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

/*+----------------------------------------------------------------------
 ||
 ||  Class Prog1B
 ||
 ||         Author:  Ben Brewer
 ||
 ||        Purpose:  takes in a bin file to parse data from
 ||
 ||  Inherits From:  None.
 ||
 ||     Interfaces:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  Just the default constructor; no arguments.
 ||
 ||  Class Methods:  void readBinary(String fileName)
 ||                  readString(RandomAccessFile file, int maxLength)
 ||                  readFixedLengthDouble(RandomAccessFile file)
 ||                  test(String fileName)
 ||                  printFirstThree(RandomAccessFile binFile)
 ||                  printLastThree(RandomAccessFile binFile)
 ||                  printMiddleThree(RandomAccessFile binFile)
 ||                  printTopTen(RandomAccessFile binFile)
 ||                  searchCrater(RandomAccessFile binFile)
 ||                  binaryExponentialSearch(RandomAccessFile binFile, String targetName)
 ||                  binarySearch(RandomAccessFile binFile, int i, int numberOfRecords, String targetName)
 ||                  printCraterInfo(RandomAccessFile binFile, int seekOffset)
 ||
 ||  Inst. Methods:  None.       
 ||
 ++-----------------------------------------------------------------------*/

public class Prog1B {


    /*---------------------------------------------------------------------
        |  Method void readBinary(String fileName)
        |
        |  Purpose:  reads and passes binaryIO file into appropriate methods.
        |            prints helpful error messages if there was a problem
        |            opening or closing the binaryIO file
        |
        |  Pre-condition:  The file fileName exists in the current
        |                  directory and the user has given the name of the 
        |                  binFile file as a command line argument. First and last
        |                  index's are Strings, everything else is doubles
        |
        |  Post-condition: None.
        |
        |  Parameters: None.
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void readBinary(String fileName) {
        RandomAccessFile binFile = null; // initializes binFile to be changed using fileName
        try {
            binFile = new RandomAccessFile(fileName + ".bin", "r");
            printFirstThree(binFile);
            printMiddleThree(binFile);
            printLastThree(binFile);
            printTopTen(binFile);
            searchCrater(binFile);

        } catch (IOException e) {
            System.out.println(fileName + " does not exist");
            System.out.println("Incorrect Usage: java Prog1B <filename>");
        } finally {
            try {
                if (binFile != null) {
                    binFile.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing bin file");
                e.printStackTrace();
            }
        }
    }

    /*---------------------------------------------------------------------
        |  Method String readString(RandomAccessFile file, int maxLength)
        |
        |  Purpose:  Helper function, reads a binary byte array and returns
        |                             string type of it
        |
        |  Pre-condition:  RandomAccessFile that is passed in is valid, the maxLength
        |                  is the length of the longest string at that index of the
        |                  record.
        |
        |  Post-condition: The string returned will still have trailing characters
        |                  even if it is not the max length.
        |
        |  Parameters: RandomAccessFile file - bin file to parse
        |              int maxLength - longest string length at the index
        |                              of the string we are reading
        |
        |  Returns: String(bytes) - String human readable form of the 
        |                           byte array 
        |
        *-------------------------------------------------------------------*/
    private static String readString(RandomAccessFile file, int maxLength) throws IOException {
        byte[] bytes = new byte[maxLength]; // creates byte array of correct length
        file.readFully(bytes);
        return new String(bytes); // String to return
    }


    /*---------------------------------------------------------------------
        |  Method double readFixedLengthDouble(RandomAccessFile file)
        |
        |  Purpose:  Helper function, reads a binary byte array and returns
        |                             double type of it
        |
        |  Pre-condition:  RandomAccessFile that is passed in is valid
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile file - bin file to parse
        |
        |  Returns: ByteBuffer.wrap(bytes).getDouble() - double type
        |           of byte array
        |
        *-------------------------------------------------------------------*/
    private static double readFixedLengthDouble(RandomAccessFile file) throws IOException {
        byte[] bytes = new byte[8]; // byte array of preset size of 8 for doubles
        file.readFully(bytes);
        return ByteBuffer.wrap(bytes).getDouble(); // double to be returned
    }

    /*---------------------------------------------------------------------
        |  Method void test(String fileName)
        |
        |  Purpose: used to print and test values to ensure they are correct
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: String fileName - name of bin file
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void test(RandomAccessFile binFile) throws IOException {
        long binFileLength = binFile.length(); // used to print bin file length
        System.out.println("binFile length: " + binFileLength);
        int longestString1 = binFile.readInt(); // used to print the first index's longest string over all the records
        System.out.println("String1 length: " + longestString1);
        int longestString2 = binFile.readInt(); // used to print the last index's longest string over all the records
        System.out.println("String2 length: " + longestString2);

        long recordLength = longestString1 + longestString2 + (8*8); // used to print the length of one record
        System.out.println("record length in bytes: " + recordLength);
        long numberOfRecords = binFile.length() / recordLength; // used to print the total number of records
        System.out.println("number of records: " + numberOfRecords);
    }

     /*---------------------------------------------------------------------
        |  Method void printFirstThree(RandomAccessFile binFile)
        |
        |  Purpose: used to print the first three craters
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printFirstThree(RandomAccessFile binFile) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        int numberOfRecords = (int) ((binFile.length() - 8) / recordLength); // total number of records
        int seekOffset = 8; // seek offset intialized to 8 to offset for the two doubles used to store data
        if (numberOfRecords > 3) {
            numberOfRecords = 3;
        }
        System.out.println("Here are the first " + numberOfRecords + " craters listed:");
        for (int i = 1; i <= numberOfRecords; i++) {
            printCraterInfo(binFile, seekOffset);
            seekOffset += recordLength;
        }
        System.out.println();
    }


     /*---------------------------------------------------------------------
        |  Method void printLastThree(RandomAccessFile binFile)
        |
        |  Purpose: used to print the last three craters
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printLastThree(RandomAccessFile binFile) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        int recordsToPrint = (int) ((binFile.length() - 8) / recordLength); // total number of records
        if (recordsToPrint > 3) {
            recordsToPrint = 3;
        }
        int seekOffset = (int) (binFile.length() - (recordsToPrint * recordLength)); // seek offset to set file pointer
        // before the last three records

        System.out.println("Here are the last " + recordsToPrint + " craters listed:");

        for (int i = 1; i <= recordsToPrint; i++) {
            printCraterInfo(binFile, seekOffset);
            seekOffset += recordLength;
        }
        System.out.println();
        System.out.println("total records: " + (int) ((binFile.length() - 8) / recordLength)); // prints number of total records
        System.out.println();
    }


     /*---------------------------------------------------------------------
        |  Method void printMiddleThree(RandomAccessFile binFile)
        |
        |  Purpose: used to print the middle three craters, prints 4 if
        |           number of craters is even
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printMiddleThree(RandomAccessFile binFile) throws IOException {

        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        int totalRecords = (int) ((binFile.length() - 8) / recordLength); // total number of records
        int recordsToPrint = 0; // number of records to print, changes after checking total number of records
        boolean oddNumberOfRecords = true; // flag to track if there is an even or odd number of records
        if (totalRecords <= 3) {
            recordsToPrint = totalRecords;
        } else if (totalRecords % 2 == 0 && totalRecords >= 4) {
            oddNumberOfRecords = false;
            recordsToPrint = 4;
        } else {
            recordsToPrint = 3;
        }
        int middleRecord = totalRecords / 2; // index for the middle record

        System.out.println("Here are the middle " + recordsToPrint + " craters listed:");
        if (oddNumberOfRecords) {
            int seekOffset = (int) (8 +  ((middleRecord - 1) * recordLength));
            for (int i = 1; i <= recordsToPrint; i++) {
                printCraterInfo(binFile, seekOffset);
                seekOffset += recordLength;
            }
        } else {
            int seekOffset = (int) (8 +  ((middleRecord - 2) * recordLength));
            for (int i = 1; i <= recordsToPrint; i++) {
                printCraterInfo(binFile, seekOffset);
                seekOffset += recordLength;
            }
        }
        System.out.println();
    }

     /*---------------------------------------------------------------------
        |  Method void printTopTen(RandomAccessFile binFile)
        |
        |  Purpose: used to print the top 10 craters in 
        |           descending order by their apparent depth.
        |           Prints more than 10 if there are ties at the
        |           10th position
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printTopTen(RandomAccessFile binFile) {
        try {
            binFile.seek(0);
            int longestString1 = binFile.readInt(); // longest string at first index of all records
            int longestString2 = binFile.readInt(); // longest string at last index of all records
            int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
            int numberOfRecords = (int) ((binFile.length() - 8) / recordLength); // total number of records

            List<Double> allCraterDepths = new ArrayList<>(); // stores all the crater depths to be parsed
            List<Integer> recordIndices = new ArrayList<>(); // stores all the record indicies to be parsed

            for (int i = 0; i < numberOfRecords; i++) {
                binFile.seek(8 + i * recordLength + 60);
                double tempCraterDepth = binFile.readDouble(); // double of the current crater depth
                allCraterDepths.add(tempCraterDepth);
                recordIndices.add(i);
            }

            List<Integer> sortedIndices = new ArrayList<>(recordIndices); // copy of recordIndicies to be sorted
            sortedIndices.sort((i1, i2) -> Double.compare(allCraterDepths.get(i2), allCraterDepths.get(i1)));

            System.out.println("Top craters based on depth:");

            int tieStartIndex = 10; // index to track the start of the tie index
            for (int i = 0; i < Math.min(10, sortedIndices.size()); i++) {
                int currentIndex = sortedIndices.get(i); // gets the current index in the sortedIndicies ArrayList
                double currentDepth = allCraterDepths.get(currentIndex); // gets the current depth at the current index

                printCraterInfo(binFile, (8 + (currentIndex * recordLength)));

                if (i == 9) {
                    while (tieStartIndex < sortedIndices.size() && allCraterDepths.get(sortedIndices.get(tieStartIndex)) == currentDepth) {
                        printCraterInfo(binFile, (8 + (sortedIndices.get(tieStartIndex) * recordLength)));
                        tieStartIndex++;
                    }
                }
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------
        |  Method void searchCrater(RandomAccessFile binFile)
        |
        |  Purpose: takes user input and searches for a crater
        |           by its name
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void searchCrater(RandomAccessFile binFile) throws IOException{
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        int numberOfRecords = (int) ((binFile.length() - 8) / recordLength); // total number of records
        String targetName = ""; // target name to be changed after scanner is initialized and called
        int craterIndex = 0; // initialization of current crater index
        Scanner scanner = new Scanner(System.in); // scanner object to get user input
        test(binFile);
        while(true){
            System.out.println("Enter crater name (or type 'e' to quit): ");
            targetName = scanner.nextLine().trim();
            if (targetName.equalsIgnoreCase("e")) {
                break;
            }
            craterIndex = binaryExponentialSearch(binFile, targetName);
            if (craterIndex == -1) {
                System.out.println("no crater found");
                System.out.println();
            } else {
                System.out.println("Crater found:");
                printCraterInfo(binFile, (8+ (craterIndex * recordLength)));
                System.out.println();
            }
        }
        scanner.close();
    }

    /*---------------------------------------------------------------------
        |  Method int binaryExponentialSearch(RandomAccessFile binFile, String targetName)
        |
        |  Purpose: calls binarySearch on a subarray of the records and 
        |           returns the index of the crater if it is found
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |              String targetName - target to search for
        |
        |  Returns: int currIndex - index of the crater if it is found, else -1
        |
        *-------------------------------------------------------------------*/
    private static int binaryExponentialSearch(RandomAccessFile binFile, String targetName) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        int numberOfRecords = (int) ((binFile.length() - 8) / recordLength); // total number of records
        
        if (numberOfRecords == 0) {
            System.out.println("No records available to search.");
            return -1;
        }
        int currIndex = 0; // current index which changes exponentially
        int seekOffset = 0; // initialized seek offset to set file pointer
        for (int i = 0; i < numberOfRecords; i++) 
        {
            currIndex = (int) (2 * (Math.pow(2, i) - 1));
            if (currIndex >= numberOfRecords) 
            {
                currIndex = numberOfRecords - 1;
            }
            seekOffset = 8 + currIndex * recordLength;
            binFile.seek(seekOffset);
            String currentCrater = readString(binFile, longestString1).trim(); // current crater name to compare
            if (currIndex >= numberOfRecords || currentCrater.compareTo(targetName) >= 0) {
                if (currentCrater.equalsIgnoreCase(targetName)) {
                    return currIndex;
                }
                return binarySearch(binFile, i, numberOfRecords, targetName);
            }
        }
        return -1;
    }

    /*---------------------------------------------------------------------
        |  Method int binarySearch(RandomAccessFile binFile, int i, int numberOfRecords, String targetName)
        |
        |  Purpose: performs a binary search on the subarray passed in by the given indexs
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |              String targetName - target to search for
        |
        |  Returns: int middleIndex - index of the crater if it is found, else -1
        |
        *-------------------------------------------------------------------*/
    private static int binarySearch(RandomAccessFile binFile, int i, int numberOfRecords, String targetName) throws IOException {
        int fromIndex = (int) (2 * (Math.pow(2,i-1)-1)) + 1; // start index
        int toIndex = (int) Math.min((2 * (Math.pow(2,i) - 1)) -1, numberOfRecords-1); // end index
        binFile.seek(0);
        int longestString1 = binFile.readInt(); // longest string at first index of all records
        int longestString2 = binFile.readInt(); // longest string at last index of all records
        int recordLength = longestString1 + longestString2 + (8 * 8); // length of one record
        while (fromIndex <= toIndex) {
            int middleIndex = (fromIndex + ((toIndex - fromIndex) / 2)); // middle index between start and end
            int seekOffset = (8 + (middleIndex * recordLength)); // sets file pointer to start of middle index
            binFile.seek(seekOffset);
            String currentCrater = readString(binFile, longestString1).trim(); // current crater name

            if (currentCrater.equalsIgnoreCase(targetName)) {
                return middleIndex;
            } else if (currentCrater.compareTo(targetName) < 0) {
                fromIndex = middleIndex + 1;
            } else {
                toIndex = middleIndex -1;
            }
        }
        return -1;
    }

    /*---------------------------------------------------------------------
        |  Method void printCraterInfo(RandomAccessFile binFile, int seekOffset)
        |
        |  Purpose: prints out specific information on a crater
        |
        |  Pre-condition:  binFile exists in current directory
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile binFile - binaryIO file to parse
        |              int seekOffset - location in bytes of crater to print
        |
        |  Returns: None.
        |
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

    public static void main(String[] args) {
        String fileName = "";
        if (args.length != 1) {
            System.out.println("Incorrect Usage: java Prog1B <filename>");
        }
        else {
            fileName = args[0];
        }
        readBinary(fileName);
    }
}