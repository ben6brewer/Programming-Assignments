/*
 * Prog1A.java -- this program takes in a csv file name as a command
 *                line argument and converts and writes it to a bin
 *                file. It does this using the RandomAccessFile (RAF)
 *                class in java.io
 *
 * Author:     Ben Brewer
 * Instructor: Lester McCann
* T/A:         Samantha Cox
 * Course:     CSC 460 Database Design
 *
 * First Version: 01/17/2024
 * Final Version: 01/24/2024
 */

import java.util.*;
import java.io.*;

/*+----------------------------------------------------------------------
 ||
 ||  Class Prog1A
 ||
 ||         Author:  Ben Brewer
 ||
 ||        Purpose:  takes in a csv and writes / converts content to a 
 ||                  bin file.
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
 ||  Class Methods:  void printFile(String fileName)
 ||                  ArrayList<ArrayList<String>> readCSV(String fileName)
 ||                  void writeBinary(ArrayList<ArrayList<String>> csvContent, String fileName)
 ||                  ArrayList<ArrayList<String>> sortCSV(int columnToSort, ArrayList<ArrayList<String>> csvContent)
 ||
 ||  Inst. Methods:  int getLongestRecordLength(String fileName)
 ||                  int getLongestString(int itemIndex, String fileName)
 ||
 ++-----------------------------------------------------------------------*/

public class Prog1A {

    /*---------------------------------------------------------------------
        |  Method printFile(String fileName)
        |
        |  Purpose:  Helper function, prints out the file with the given directory
        |
        |  Pre-condition:  The file csvDirectory exists in the current
        |                  directory and the user has given the name of the 
        |                  csv file as a command line argument
        |
        |  Post-condition: Terminal is populated with the file contents
        |
        |  Parameters: String fileName - name of file to print
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void printFile(String fileName) {
        String csvDirectory = fileName + ".csv"; // file with csv tag appended
        try (BufferedReader reader = new BufferedReader(new FileReader(csvDirectory)))
        {
            String line; // initialized mutable line to store each line in the file
            while ((line = reader.readLine()) != null) 
            {
                System.out.println(line);
            }
        } 
        catch (IOException exception) 
        {
            System.out.println("An error occurred while reading the file: " + exception.getMessage());
        }
    }

    /*---------------------------------------------------------------------
        |  Method getLongestRecordLength(String fileName)
        |
        |  Purpose:  Helper function, returns the length of a single record
        |            by looping through all of the records and grabbing
        |            the length of the longest record
        |
        |  Pre-condition:  The file csvDirectory exists in the current
        |                  directory and the user has given the name of the 
        |                  csv file as a command line argument
        |
        |  Post-condition: None.
        |
        |  Parameters: String fileName - name of file to get data from
        |
        |  Returns: int longestRecordLength - longest length of all the records, else 0
        *-------------------------------------------------------------------*/
    private static int getLongestRecordLength(String fileName) {
        int longestRecordLength = 0; // initialized longestRecordLength to 0, will be changed after reading in file

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName + ".csv"))) {
            String line; // String to hold data from each line
            
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(","); // String array to split each comma seperated value in each line
                if (splitLine.length > longestRecordLength) {
                    longestRecordLength = splitLine.length;
                }
            }
            return longestRecordLength;
        } catch (IOException exception) {
            System.out.println("An error occurred while reading the file: " + exception.getMessage());
            return 0;
        }
    }

    /*---------------------------------------------------------------------
        |  Method getLongestString(int itemIndex)
        |
        |  Purpose:  Helper function, returns the longest string at a given
        |            index by looping through all of the records and comparing
        |            the lengths of the string at that index
        |
        |  Pre-condition:  The file csvDirectory exists in the current
        |                  directory and the user has given the name of the 
        |                  csv file as a command line argument. A string is 
        |                  at the index given as a parameter
        |
        |  Post-condition: None.
        |
        |  Parameters: int itemIndex - index of the item to search for the longest
        |                              string at
        |              String fileName - name of file to get data from
        |
        |  Returns: largestStringLength -- length of longest string at that index
        |                                  across all of the records
        *-------------------------------------------------------------------*/
    private static int getLongestString(int itemIndex, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName + ".csv"))) {
            String line; // String to hold the data from each line
            int largestStringLength = 0; // int to hold the largest string length, will change after reading file
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                if (splitLine.length < itemIndex + 1) {
                    continue;
                }
                String currWord = splitLine[itemIndex]; // String to hold the current word
                int currWordLength = (currWord != null) ? currWord.length() : 0; // int to get the length of the current word
                if (currWordLength > largestStringLength) {
                    largestStringLength = currWordLength;
                }
            }
            return largestStringLength;
        } catch (IOException exception) {
            System.out.println("An error occurred while reading the file: " + exception.getMessage());
            return 0;
        }
    }


    /*---------------------------------------------------------------------
        |  Method ArrayList<ArrayList<String>> readCSV(String fileName)
        |
        |  Purpose:  Helper function, parses the csv and returns a 2D
        |            ArrayList populated with Strings. Each inner ArrayList
        |            acts as a record holding these Strings with each index
        |            acting as a column
        |
        |  Pre-condition:  The file csvDirectory exists in the current
        |                  directory and the user has given the name of the 
        |                  csv file as a command line argument. First and last
        |                  index's are Strings, everything else is doubles
        |
        |  Post-condition: None.
        |
        |  Parameters: String fileName - name of file to read data from
        |
        |  Returns: ArrayList<ArrayList<String>> finalArrayList - 2D ArrayList
        |                                        of csv data
        |
        *-------------------------------------------------------------------*/
    private static ArrayList<ArrayList<String>> readCSV(String fileName) {
        ArrayList<ArrayList<String>> finalArrayList = new ArrayList<>(); // ArrayList to populate and return
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName + ".csv"))) {
            reader.readLine(); // skip first line, has useless data
            String line; // String to hold data for each line in the CSV
            while ((line = reader.readLine()) != null) {
                ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(","))); // internal ArrayList to be added to finalArrayList
                if (splitLine.size() < 10) {
                    splitLine.add("");
                }
                finalArrayList.add(splitLine);
            }
        } catch (IOException exception) {
            System.out.println("An error occurred while reading the file: " + exception.getMessage());
            return null;
        }
        return finalArrayList;
    }

     /*---------------------------------------------------------------------
        |  Method void writeBinary(ArrayList<ArrayList<String>> csvContent, String fileName)
        |
        |  Purpose: writes csv content in binary to bin file
        |
        |  Pre-condition:  csvContent is a 2D ArrayList with records,
        |                  the fileName given by the user as a command
        |                  line argument is valid
        |
        |  Post-condition: A bin file with the conversion of the contents
        |                  from the csv file given by the user exists
        |
        |  Parameters: ArrayList<ArrayList<String>> csvContent - 2D
        |              ArrayList of records from the csv
        |              String fileName - name of file to write to
        |
        |  Returns: None.
        |
        *-------------------------------------------------------------------*/
    private static void writeBinary(ArrayList<ArrayList<String>> csvContent, String fileName) {
        File findFile = new File(fileName + ".bin"); // creates new bin file
        if (findFile.exists()) {
            findFile.delete();
        }
        try (RandomAccessFile binaryFile = new RandomAccessFile(findFile, "rw")) {            
            binaryFile.writeInt(getLongestString(0, fileName));
            binaryFile.writeInt(getLongestString(9, fileName));
            for (ArrayList<String> object : csvContent) {
                for (int i = 0; i < object.size(); i++) {
                    if (i == 0) {
                        StringBuffer tempString = new StringBuffer(object.get(i)); // temporary string to get length of and write bytes
                        tempString.setLength(getLongestString(0, fileName));
                        binaryFile.writeBytes(tempString.toString());
                    } else if (i == 9) {
                        StringBuffer tempString = new StringBuffer(object.get(i)); // temporary string to get length of and write bytes
                        tempString.setLength(getLongestString(9, fileName));
                        binaryFile.writeBytes(tempString.toString());
                    } else {
                        binaryFile.writeDouble(Double.valueOf(object.get(i)));
                    }
                }
            }
            binaryFile.close();
        } catch (IOException e) {
            System.out.println("Could not create bin file " + e.getMessage());
            System.exit(-1);
        }
    }

    /*---------------------------------------------------------------------
        |  Method void ArrayList<ArrayList<String>> sortCSV(int columnToSort, ArrayList<ArrayList<String>> csvContent)
        |
        |  Purpose: sorts the csv in alphabetical order based on the given index of
        |           each record in the 2D ArrayList
        |
        |  Pre-condition:  csvContent is a 2D ArrayList with records,
        |                  the given index of each record is a string
        |
        |  Post-condition: the 2D ArrayList is now sorted in alphabetical order
        |                  based on the given index
        |
        |  Parameters: int columnToSort - column of string in record to sort by
        |              ArrayList<ArrayList<String>> csvContent - 2D ArrayList to sort
        |
        |  Returns: csvContent ArrayList<ArrayList<String>> - sorted 2D ArrayList
        |
        *-------------------------------------------------------------------*/
    private static ArrayList<ArrayList<String>> sortCSV(int columnToSort, ArrayList<ArrayList<String>> csvContent) {
        csvContent.sort(Comparator.comparing(row -> row.get(columnToSort).toString()));
        return csvContent;
    }

    public static void main(String[] args) {
        String fileName = ""; // name of file to be assigned after reading in command line argument
        if (args.length != 1) {
            System.out.println("Incorrect Usage: java Prog1A <filename>");
        }
        else {
            fileName = args[0];
        }
        writeBinary(sortCSV(0, readCSV(fileName)), fileName);
    }
}