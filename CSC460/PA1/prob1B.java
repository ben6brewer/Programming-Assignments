/*
 * Prog1B.java -- this program takes in a bin file name as a command
 *                line argument and converts it to a 2D ArrayList of
 *                records which is used to get specific data such as,
 *                top, middle, and bottom crater depths. As well as
 *                taking in input from the user to search for a crater
 *
 * Author: Ben Brewer
 *
 * First Version: 01/21/2024
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
 ||        Purpose:  takes in a bin creates a searchable 2D ArrayList of
 ||                  records
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
 ||  Class Methods:  ArrayList<ArrayList<String>> readBinary()
 ||                  String readString(RandomAccessFile file, int maxLength)
 ||                  double readFixedLengthDouble(RandomAccessFile file)
 ||                  void test(String fileName)
 ||                  printFirstThree(ArrayList<ArrayList<String>> binFileContent)
 ||                  printLastThree(ArrayList<ArrayList<String>> binFileContent)
 ||                  printMiddleThree(ArrayList<ArrayList<String>> binFileContent)
 ||                  printTopTen(ArrayList<ArrayList<String>> binFileContent)
 ||                  binaryExponentialSearch(ArrayList<ArrayList<String>> binFileContent)
 ||                  binarySearch(ArrayList<ArrayList<String>> binFileContent, int fromIndex, int toIndex, String targetName)
 ||                  void printCraterInfo(int index, ArrayList<ArrayList<String>> binFileContent)
 ||
 ||  Inst. Methods:  None.       
 ||
 ++-----------------------------------------------------------------------*/

public class Prog1B {


    /*---------------------------------------------------------------------
        |  Method ArrayList<ArrayList<String>> readBinary()
        |
        |  Purpose:  Parses the created bin file and returns a 2D
        |            ArrayList populated with Strings. Each inner ArrayList
        |            acts as a record holding these Strings with each index
        |            acting as a column
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
        |  Returns: ArrayList<ArrayList<String>> finalArrayList - 2D ArrayList
        |                                        of csv data
        |
        *-------------------------------------------------------------------*/
    private static ArrayList<ArrayList<String>> readBinary(String fileName) {
        ArrayList<ArrayList<String>> finalArrayList = new ArrayList<>();
        RandomAccessFile binFile = null;

        try {
            binFile = new RandomAccessFile(fileName + ".bin", "r");

            printFirstThree(binFile);
            printLastThree(binFile);
            printMiddleThree(binFile);
            binaryExponentialSearch(binFile);

        } catch (IOException e) {
            System.out.println("Error creating bin file");
            e.printStackTrace();
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

        return finalArrayList;
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
        byte[] bytes = new byte[maxLength];
        file.readFully(bytes);
        return new String(bytes);
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
        byte[] bytes = new byte[8];
        file.readFully(bytes);
        return ByteBuffer.wrap(bytes).getDouble();
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
    private static void test(String fileName) {
        RandomAccessFile binFile = null;
        try {
            binFile = new RandomAccessFile(fileName + ".bin", "r");
            long binFileLength = binFile.length();
            System.out.println("binFile length: " + binFileLength);
            int longestString1 = binFile.readInt();
            System.out.println("String1 length: " + longestString1);
            int longestString2 = binFile.readInt();
            System.out.println("String2 length: " + longestString2);

            long recordLength = longestString1 + longestString2 + (8*8);
            System.out.println("record length in bytes: " + recordLength);
            long numberOfRecords = binFile.length() / recordLength;
            System.out.println("number of records: " + numberOfRecords); // 8716

        } catch (IOException e) {
            System.out.println("Error creating bin file");
            e.printStackTrace();
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
        |  Method void printFirstThree(ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: used to print the first three craters
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printFirstThree(RandomAccessFile binFile) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt();
        int longestString2 = binFile.readInt();
        int recordLength = longestString1 + longestString2 + (8 * 8);
        int numberOfRecords = (int) ((binFile.length() - 8) / recordLength);
        int seekOffset = 8;
        if (numberOfRecords > 3) {
            numberOfRecords = 3;
        }
        System.out.println("Here are the first " + numberOfRecords + " craters listed:");
        for (int i = 1; i <= numberOfRecords; i++) {
            printCraterInfo(binFile, seekOffset, longestString1, longestString2);
            seekOffset += recordLength;
        }
        System.out.println();
    }


     /*---------------------------------------------------------------------
        |  Method void printLastThree(ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: used to print the last three craters
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printLastThree(RandomAccessFile binFile) throws IOException {
        binFile.seek(0);
        int longestString1 = binFile.readInt();
        int longestString2 = binFile.readInt();
        int recordLength = longestString1 + longestString2 + (8 * 8);
        int recordsToPrint = (int) ((binFile.length() - 8) / recordLength);
        if (recordsToPrint > 3) {
            recordsToPrint = 3;
        }
        int seekOffset = (int) (binFile.length() - (recordsToPrint * recordLength));

        System.out.println("Here are the last " + recordsToPrint + " craters listed:");

        for (int i = 1; i <= recordsToPrint; i++) {
            printCraterInfo(binFile, seekOffset, longestString1, longestString2);
            seekOffset += recordLength;
        }
        System.out.println();
    }


     /*---------------------------------------------------------------------
        |  Method void printMiddleThree(ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: used to print the middle craters, if size is even then prints 
        |           the middle 4
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printMiddleThree(RandomAccessFile binFile) throws IOException {

        binFile.seek(0);
        int longestString1 = binFile.readInt();
        int longestString2 = binFile.readInt();
        int recordLength = longestString1 + longestString2 + (8 * 8);
        int totalRecords = (int) ((binFile.length() - 8) / recordLength);
        int recordsToPrint = 0;
        boolean oddNumberOfRecords = true;
        if (totalRecords <= 3) {
            recordsToPrint = totalRecords;
        } else if (totalRecords % 2 == 0 && totalRecords >= 4) {
            oddNumberOfRecords = false;
            recordsToPrint = 4;
        } else {
            //(totalRecords % 2 = 1 && totalRecords >= 3){
            recordsToPrint = 3;
        }
        int middleRecord = totalRecords / 2;

        System.out.println("Here are the middle " + recordsToPrint + " craters listed:");
        if (oddNumberOfRecords) {
            int seekOffset = (int) (binFile.length() - ((middleRecord + 1) * recordLength));
            for (int i = 1; i <= recordsToPrint; i++) {
                printCraterInfo(binFile, seekOffset, longestString1, longestString2);
                seekOffset += recordLength;
            }
        } else {
            int seekOffset = (int) (binFile.length() - ((middleRecord + 1) * recordLength));
            for (int i = 1; i <= recordsToPrint; i++) {
                printCraterInfo(binFile, seekOffset, longestString1, longestString2);
                seekOffset += recordLength;
            }
        }
        System.out.println();
    }

     /*---------------------------------------------------------------------
        |  Method void printTopTen(ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: used to print the top ten craters by their depth
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printTopTen(RandomAccessFile binFile) {
        try {
            binFile.seek(0);
            int longestString1 = binFile.readInt();
            int longestString2 = binFile.readInt();
            int recordLength = longestString1 + longestString2 + (8 * 8);
            int numberOfRecords = (int) ((binFile.length() - 8) / recordLength);

            List<Double> allCraterDepths = new ArrayList<>();
            List<Integer> recordIndices = new ArrayList<>();

            for (int i = 0; i < numberOfRecords; i++) {
                binFile.seek(8 + i * recordLength + 60);
                double tempCraterDepth = binFile.readDouble();
                allCraterDepths.add(tempCraterDepth);
                recordIndices.add(i);
            }

            List<Integer> sortedIndices = new ArrayList<>(recordIndices);
            sortedIndices.sort((i1, i2) -> Double.compare(allCraterDepths.get(i2), allCraterDepths.get(i1)));

            System.out.println("Top craters based on depth:");

            int tieStartIndex = 10;
            for (int i = 0; i < Math.min(10, sortedIndices.size()); i++) {
                int currentIndex = sortedIndices.get(i);
                double currentDepth = allCraterDepths.get(currentIndex);

                printCraterInfo(binFile, (8 + (currentIndex * recordLength)), longestString1, longestString2);

                if (i == 9) {
                    while (tieStartIndex < sortedIndices.size() && allCraterDepths.get(sortedIndices.get(tieStartIndex)) == currentDepth) {
                        printCraterInfo(binFile, (8 + (sortedIndices.get(tieStartIndex) * recordLength)), longestString1, longestString2);
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
        |  Method void binaryExponentialSearch(ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: uses binary exponential search to find a name in an
        |           ArrayList
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void binaryExponentialSearch(RandomAccessFile binFile) throws IOException {
        while (true) {
            binFile.seek(0);
            int longestString1 = binFile.readInt();
            int longestString2 = binFile.readInt();
            int recordLength = longestString1 + longestString2 + (8 * 8);
            int numberOfRecords = (int) ((binFile.length() - 8) / recordLength);
            
            if (numberOfRecords == 0) {
                System.out.println("No records available to search.");
                break;
            }

            System.out.println("Enter crater name (or type 'e' to quit): ");
            Scanner scanner = new Scanner(System.in);
            String targetName = scanner.nextLine().trim();

            if (targetName.equalsIgnoreCase("e")) {
                break;
            }

            boolean found = false;
            int seekOffset = 0;
            
            for (int i = 0; i < numberOfRecords; i++) {
                int currIndex = (int) (2 * (Math.pow(2, i) - 1));
                if (currIndex >= numberOfRecords) {
                    currIndex = numberOfRecords - 1;
                }
                seekOffset = 8 + currIndex * recordLength;
                binFile.seek(seekOffset);
                String currentCrater = readString(binFile, longestString1);

                if (currIndex >= numberOfRecords || currentCrater.compareToIgnoreCase(targetName) >= 0) {
                    if (currentCrater.equalsIgnoreCase(targetName)) {
                        found = true;
                        printCraterInfo(binFile, seekOffset, longestString1, longestString2);
                        System.out.println();
                    }

                    if (!found) {
                        binarySearch(binFile, currIndex, numberOfRecords, targetName);
                    }
                }
            }
            scanner.close();
        }
    }


    /*---------------------------------------------------------------------
        |  Method int binarySearch(ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: uses binary search to find a name in an ArrayList
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |              int fromIndex - starting index to search from
        |              int toIndex - ending index to search to
        |              String targetName - name to search for
        |
        |  Returns: int - index of the ArrayList containing the string, else -1 if DNE
        |
        *-------------------------------------------------------------------*/
    private static int binarySearch(RandomAccessFile binFile, int fromIndex, int toIndex, String targetName) throws IOException {
    if (toIndex >= fromIndex) {
        binFile.seek(0);
        int longestString1 = binFile.readInt();
        int longestString2 = binFile.readInt();
        int recordLength = longestString1 + longestString2 + (8 * 8);
        int totalRecords = (int) (binFile.length() / recordLength);
        int middleIndex = (fromIndex + ((toIndex - fromIndex) / 2));
        int seekOffset = (8 + (middleIndex * recordLength));
        binFile.seek(seekOffset);
        String currentCrater = readString(binFile, longestString1);

        if (currentCrater.equalsIgnoreCase(targetName)) {
            return middleIndex;
        }

        if (currentCrater.compareToIgnoreCase(targetName) > 0) {
            return binarySearch(binFile, middleIndex, fromIndex - 1, targetName);
        } else {
            return binarySearch(binFile, middleIndex + 1, toIndex, targetName);
        }
    }

    return -1;
}


    /*---------------------------------------------------------------------
        |  Method void printCraterInfo(int index, ArrayList<ArrayList<String>> binFileContent)
        |
        |  Purpose: prints the revlevant info belonging to a crater
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: int index - index of the crater to print
        |              ArrayList<ArrayList<String>> binFileContent - 2D ArrayList
        |              of craters
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void printCraterInfo(RandomAccessFile binFile, int seekOffset, int longestString1, int longestString2) throws IOException {
        binFile.seek(seekOffset);
        String name = readString(binFile, longestString1);
        double diameter = readFixedLengthDouble(binFile);
        binFile.seek(binFile.getFilePointer() + (2 * 8));
        double depth = readFixedLengthDouble(binFile);
        binFile.seek(binFile.getFilePointer() + (4 * 8));

        String age = readString(binFile, longestString2).trim();
        if (age == null || age.isEmpty()) {
            age = "unknown";
        }

        System.out.print("[" + name + "] [" + diameter + "] [" + depth + "] [" + age + "]");

        System.out.println();
    }


    public static void main(String[] args) {
        // if (args.length != 1) {
        //     System.out.println("Incorrect Usage: ./Prog1A.java <filename>");
        // }
        // else {
        //     fileName = args[0];
        // }
        String fileName = "lunarcraters";
        readBinary(fileName);
        // test(fileName);
        // printFirstThree(binFileContent);
        // printLastThree(binFileContent);
        // printMiddleThree(binFileContent);
        // printTopTen(binFileContent);
        // binaryExponentialSearch(binFileContent);
    }
}