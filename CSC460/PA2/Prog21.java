/*
 * Prog21.java -- this program takes in a binary file name as a command
 *                line argument and creates and creates a linear
 *                hashing index file named "lhl.idx". The first
 *                string in the bin file (crater name) are the keys
 *                and their location within the binary file are the
 *                values
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

/*+----------------------------------------------------------------------
 ||
 ||  Class Prog21
 ||
 ||         Author:  Ben Brewer
 ||
 ||        Purpose:  creates a linear hashing index file based on the
 ||                  binary file "lunarcraters.bin" or whatever file is
 ||                  passed in as a command line argument
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
 ||  Class Methods:  RandomAccessFile updateBinaryIndexFile(int longestCraterName, int numberOfBuckets)
 ||                  void addNewBucket(RandomAccessFile indexBinaryFile, int longestCraterName)
 ||                  void printBucketInfo(int numberOfBuckets, RandomAccessFile indexBinaryFile, int longestCraterName)
 ||                  int hashBinaryFile(RandomAccessFile indexBinaryFile, RandomAccessFile binFile, int h, int numberOfBuckets,double numberOfRecords, int longestCraterName, int recordLength)
 ||                  int overwriteRecord(String craterName, int binIndex, int binHashIndex, RandomAccessFile indexBinaryFile, int longestCraterName)
 ||                  int hash(String name, int h)
 ||                  
 ||
 ||  Inst. Methods:  int getRecordsInBucket(RandomAccessFile indexBinaryFile, int startOfBucket, int longestCraterName)
 ||
 ++-----------------------------------------------------------------------*/

public class Prog21 {

    /*---------------------------------------------------------------------
        |  Method updateBinaryIndexFile(int longestCraterName, int numberOfBuckets)
        |
        |  Purpose:  This function will update the binary file by adding the
        |            correct number of empty buckets
        |
        |  Pre-condition: None.
        |
        |  Post-condition: Index binary file exists. index binary file increased 
        |                    in size by 2x empty buckets
        |
        |  Parameters: int longestCraterName - length of the longest string used 
        |                  to set padding for empty index
        |              int numberOfBuckets - number of buckets to resize to
        |
        |  Returns: RandomAccessFile indexBinaryFile - updated index file
        *-------------------------------------------------------------------*/
    private static RandomAccessFile updateBinaryIndexFile(int longestCraterName, int numberOfBuckets) throws IOException {
        String binFileName = "lhl.idx"; // hard coded index file name given in spec
        File createdFile = new File(binFileName); // creates new binary index file
        if (createdFile.exists()) {
            createdFile.delete();
        }
        RandomAccessFile indexBinaryFile = new RandomAccessFile(createdFile, "rw"); // creates binary index file
        for (int i = 0; i < numberOfBuckets; i++) {
            addNewBucket(indexBinaryFile, longestCraterName);
        }
        return indexBinaryFile;
    }

    /*---------------------------------------------------------------------
        |  Method addNewBucket(RandomAccessFile indexBinaryFile, int longestCraterName)
        |
        |  Purpose: adds a new empty bucket of size 25 indicies
        |
        |  Pre-condition:  indexBinaryFile exists and is valid
        |
        |  Post-condition: an empty bucket was appended to the index file
        |
        |  Parameters: RandomAccessFile indexBinaryFile - file to append bucket to
        |              int longestCraterName - length of longest string key to
        |                   to set padding to
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void addNewBucket(RandomAccessFile indexBinaryFile, int longestCraterName) throws IOException {
        for (int i = 0; i < 25; i ++) {
            StringBuffer tempIndexNameBuffer = new StringBuffer(); // temporary string buffer
            tempIndexNameBuffer.setLength(longestCraterName);
            indexBinaryFile.writeBytes(tempIndexNameBuffer.toString());
            indexBinaryFile.writeInt(Integer.MIN_VALUE);
        }
    }

    /*---------------------------------------------------------------------
        |  Method printBucketInfo(int numberOfBuckets, double totalRecords, RandomAccessFile indexBinaryFile, int longestCraterName)
        |
        |  Purpose:  prints required index file info given from spec
        |
        |  Pre-condition:  indexBinaryFile exists
        |
        |  Post-condition: Terminal is populated with spec requirements
        |
        |  Parameters: int numberOfBuckets - total number of buckets sized 25 indicies
        |              RandomAccessFile indexBinaryFile - index file
        |              int longestCraterName - length of longest key string
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    static void printBucketInfo(int numberOfBuckets, RandomAccessFile indexBinaryFile, int longestCraterName) throws IOException{
        System.out.println("number of buckets: " + numberOfBuckets);
        int lowestBucketCount = 25; // initialize lowest bucket count to be changed
        int highestBucketCount = 0; // initialize highest bucket count to be changed
        int realRecords = 0; // initialize real record count to be changed

        for (int i = 0; i < numberOfBuckets; i++) {
            int recordsInCurrBucket = getRecordsInBucket(indexBinaryFile, i, longestCraterName); // number of records in current bucket

            if (recordsInCurrBucket > highestBucketCount) {
                highestBucketCount = recordsInCurrBucket;
            }
            else if (recordsInCurrBucket < lowestBucketCount) {
                lowestBucketCount = recordsInCurrBucket;
            }
            realRecords += recordsInCurrBucket;
        }
        System.out.println("the number of records in the lowest–occupancy bucket: " + lowestBucketCount);
        System.out.println("the number of records in the highest–occupancy bucket: " + highestBucketCount);
        System.out.println("the mean of the occupancies across all buckets:  " + realRecords/numberOfBuckets);
    }

    /*---------------------------------------------------------------------
        |  Method getRecordsInBucket(RandomAccessFile indexBinaryFile, int startOfBucket, int longestCraterName)
        |
        |  Purpose:  getter for the number of non-empty (real) records in a bucket
        |
        |  Pre-condition:  indexBinaryFile exists
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile indexBinaryFile - file to parse
        |              int startOfBucket - index to start search from
        |              int longestCraterName - length of longest key string
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static int getRecordsInBucket(RandomAccessFile indexBinaryFile, int startOfBucket, int longestCraterName) throws IOException {
        int numberOfRealRecords = 0; // intitialized number to be changed and returned
        startOfBucket = startOfBucket * (25 * (longestCraterName+4)); // gets proper location of bucket
        for (int i = 0; i < 25; i++) {
            indexBinaryFile.seek(startOfBucket + (i * (longestCraterName+4)));
            String recordStringName = readString(indexBinaryFile, longestCraterName); // string at current record
            if (!recordStringName.trim().equals("")) {
                numberOfRealRecords += 1;
            }
        }
        return numberOfRealRecords;
    }

    /*---------------------------------------------------------------------
        |  Method hashBinaryFile(RandomAccessFile indexBinaryFile, RandomAccessFile binFile, int h ,double numberOfRecords, int longestCraterName, int recordLength)
        |
        |  Purpose:  hashes all of the records in the binary file
        |
        |  Pre-condition:  binFile and indexBinaryFile exist
        |
        |  Post-condition: None.
        |
        |  Parameters: RandomAccessFile indexBinaryFile - file to append hashed records to
        |              RandomAccessFile binFile - file to parse and hash records
        |              int h - integer used to dynamically hash
        |              double numberOfRecords - number of records in binFile
        |              int recordLength - length of one record in binFile
        |              
        |
        |  Returns: int 0 - if there are no records to be hashed, else 1 if it needs
        |                   to hash and update
        *-------------------------------------------------------------------*/
    private static int hashBinaryFile(RandomAccessFile indexBinaryFile, RandomAccessFile binFile, int h, double numberOfRecords, int longestCraterName, int recordLength) throws IOException {
        for (int i = 0; i < numberOfRecords; i++) {
            int binRecordIndex = 8 + (i*recordLength); // binary file record index
            binFile.seek(binRecordIndex);
            String craterName = readString(binFile, longestCraterName); // crater name in binary file
            int hashIndex = hash(craterName.trim(), h); // hash of the craterName
            int binIndexLocation = hashIndex * (25*(4+longestCraterName)); // location in binary index file of crater
            int needsUpdate = overwriteRecord(craterName, binRecordIndex, binIndexLocation, indexBinaryFile, longestCraterName); // boolean var used to check status
            if (needsUpdate == 1) {
                return 1;
            }
        }
        return 0;
    }

    /*---------------------------------------------------------------------
        |  Method overwriteRecord(String strName, int binIndex, int binHashIndex, RandomAccessFile indexBinaryFile, int longestCraterName)
        |
        |  Purpose:  used to overwrite an empty record with the hashed record at a 
        |            specified index
        |
        |  Pre-condition:  indexBinaryFile exists
        |
        |  Post-condition: an empty record in indexBinaryFile is overwritten
        |
        |  Parameters: String craterName - name of record use to overwrite
        |              int binIndex - index location in binary file of target
        |              int binHashIndex - index to overwrite
        |              RandomAccessFile indexBinaryFile - index file to overwrite record
        |              int longestCraterName - length of longest key string
        |
        |  Returns: int 0 - if it index was sucessfully overwritten, else 1 if there was not
        |                   an empty slot in the bucket to append to
        *-------------------------------------------------------------------*/
    private static int overwriteRecord(String craterName, int binIndex, int binIndexLocation, RandomAccessFile indexBinaryFile, int longestCraterName) throws IOException {
        indexBinaryFile.seek(binIndexLocation);
        boolean emptySlot = false; // boolean tracker for empty slot to be potentially updated
        int indexInBucket = 0; // index within the bucket
        for (int i = 0; i < 25; i++) {
            indexInBucket = i;
            int currIndexLocation = binIndexLocation + (i * (4 + longestCraterName)); // location of current index
            indexBinaryFile.seek(currIndexLocation);
            String currCraterName = readString(indexBinaryFile, longestCraterName); // string at current index
            if (currCraterName.trim().equals("")) {
                emptySlot = true;
                break;
            }
        }
        if (emptySlot == false) {
            return 1;
        }
        indexBinaryFile.seek(binIndexLocation + (indexInBucket * (4 + longestCraterName)));
        StringBuffer stringToAdd = new StringBuffer(craterName); // temp string buffer
        stringToAdd.setLength(longestCraterName);
        indexBinaryFile.writeBytes(stringToAdd.toString());
        indexBinaryFile.writeInt(binIndex);
        return 0;
    }

    /*---------------------------------------------------------------------
        |  Method hash(String name, int h)
        |
        |  Purpose:  used to calculate the hash of a given string using 
        |            the dynamic value h. This equation was given in the
        |            spec.
        |
        |  Pre-condition:  None.
        |
        |  Post-condition: None.
        |
        |  Parameters: String name - string to hash
        |              int h - dynamic variable used to help calculate hash
        |
        |  Returns: int hash - hash of the given string
        *-------------------------------------------------------------------*/
    private static int hash(String name, int h) {
        int hash = (int) (Math.abs(name.hashCode() % (Math.pow(2, h+1)))); // equation given in spec
        return hash;
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

    public static void main(String[] args) throws IOException {

        String fileName = ""; // name of file to be assigned after reading in command line argument
        if (args.length != 1) {
            System.out.println("Incorrect Usage: java Prog21 <filename>");
            System.exit(1);
        }
        else {
            fileName = args[0];
        }

        RandomAccessFile binFile = null; // binFile to pass into other functions
        try {
            binFile = new RandomAccessFile(fileName, "r"); 

            binFile.seek(0);
            int longestCraterName = binFile.readInt(); // used to print the first index's longest string over all the records
            int longestString2 = binFile.readInt(); // used to print the last index's longest string over all the records
            int recordLength = longestCraterName + longestString2 + (8*8); // used to calulate the number of buckets
            double numberOfRecords = ((binFile.length()) /  recordLength); // used to calculate number of buckets and mean
            int numberOfBuckets = 2; // starting number of buckets
            int h = 0; // starting h value
            RandomAccessFile indexBinaryFile = updateBinaryIndexFile(longestCraterName, numberOfBuckets); // index file to be passed into functions
            int needsUpdate = hashBinaryFile(indexBinaryFile, binFile, h, numberOfRecords, longestCraterName, recordLength); // boolean int to check update status
            while (needsUpdate == 1) {
                h += 1;
                numberOfBuckets *=2;
                indexBinaryFile = updateBinaryIndexFile(longestCraterName, numberOfBuckets);
                needsUpdate = hashBinaryFile(indexBinaryFile, binFile, h, numberOfRecords, longestCraterName, recordLength);
            }
            printBucketInfo(numberOfBuckets, indexBinaryFile, longestCraterName);
            indexBinaryFile.seek(indexBinaryFile.length());
            indexBinaryFile.writeInt(h);

        } catch (IOException e) {
            System.out.println(fileName + " does not exist");
            System.out.println("Incorrect Usage: java Prog21 <filename>");
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
}