import java.io.*;






public class Prog211 {

    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        RandomAccessFile binFile = new RandomAccessFile(fileName, "rw");
        RandomAccessFile indexFile = null;

        int max1 = binFile.readInt();
        int max2 = binFile.readInt();

        int numBuckets = 2;
        int H = 0;
        indexFile = createIndex(indexFile, max1, numBuckets);

        int update = fillIndexFile(binFile, indexFile, max1, max2, H);
        while (update == 1) {
            System.out.println("update = 1");
            H = H + 1;
            numBuckets = numBuckets * 2;
            indexFile = createIndex(indexFile, max1, max2);
            update = fillIndexFile(binFile, indexFile, max1, max2, H);
        }

        indexFile.seek(indexFile.length());
        indexFile.writeInt(H);

        System.out.println("Number of Buckets: " + numBuckets);
        printOthers(indexFile, H, numBuckets, max1);

        indexFile.close();
        binFile.close();
    }


    private static void printOthers(RandomAccessFile indexFile, int h, int numBuckets, int max1) throws IOException {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int total = 0;

        for (int bucket = 0; bucket < numBuckets; bucket++) {
            int starting = bucket * (25 * (max1+4));
            int records = getRecordCount(indexFile, starting, max1);

            if (records > max) {
                max = records;
            }
            if (records < min) {
                min = records;
            }
            total += records;
        }

        System.out.println("Lowest number of records in a bucket: " + min);
        System.out.println("Highest number of records in a bucket: " + max);
        System.out.println("Average number of records in a bucket: " + total/numBuckets);

    }

    private static int getRecordCount(RandomAccessFile indexFile, int starting, int max1) throws IOException {
        int retval = 0;
        indexFile.seek(starting);
        for (int i = 0; i < 25; i++) {
            indexFile.seek(starting + (i*(max1+4)));
            byte[] name = new byte[max1];
            indexFile.readFully(name);
            String strName = new String(name);
            if (strName.trim() != "") {
                retval += 1;
            }
        }
        return retval;
    }

    private static int fillIndexFile(RandomAccessFile binFile, RandomAccessFile indexFile, int max1, int max2, int h) throws IOException {
        binFile.seek(0);
        max1 = binFile.readInt();
        max2 = binFile.readInt();

        int recordLength = max1 + max2 + (8*8);
        long numberOfRecords = binFile.length() / recordLength;

        for (int i = 0; i < numberOfRecords; i++) {
            int binIndex = 8 + (i*recordLength);
            binFile.seek(binIndex);
            byte[] name = new byte[max1];
            binFile.readFully(name);
            String strName = new String(name);

            int hashIndex = getHashCode(strName.trim(), h);
            int binHashIndex = hashIndex * (25*(4+max1));

            int update = writeToIndex(strName, binIndex, binHashIndex, indexFile, max1);
            if (update == 1) {
                return 1;
            }
        }
        return 0;
    }

    private static int writeToIndex(String strName, int binIndex, int binHashIndex, RandomAccessFile indexFile, int max1) throws IOException {
        indexFile.seek(binHashIndex);
        int i;

        for (i = 0; i < 25; i++) {
            int currBinIndex = binHashIndex + (i * (4 + max1));
            indexFile.seek(currBinIndex);

            byte[] nameCheck = new byte[max1];
            int bytesRead = indexFile.read(nameCheck);

            if (bytesRead == -1) {
                // Handle end of file or unexpected situation
                return 1;
            } else if (bytesRead < max1) {
                // Handle incomplete record
                return 1;
            }

            String strNameCheck = new String(nameCheck);
            if (strNameCheck.trim().isEmpty()) {
                break;
            }
        }

        if (i == 25) {
            return 1; // Unable to find an empty slot
        }

        indexFile.seek(binHashIndex + (i * (4 + max1)));
        StringBuffer name = new StringBuffer(strName);
        name.setLength(max1);
        indexFile.writeBytes(name.toString());
        indexFile.writeInt(binIndex);

        indexFile.seek(binHashIndex);

        return 0; // Successfully wrote to index
    }



    private static int getHashCode(String strName, int h) {
        int hash = (int) (Math.abs(strName.hashCode() % (Math.pow(2, h+1))));
        return hash;
    }

    private static RandomAccessFile createIndex(RandomAccessFile indexFile, int max1, int numBuckets) throws IOException {
        File fileRef = null;

        fileRef = new File("lhl.idx");
        if (fileRef.exists()) {
            fileRef.delete();
        }

        indexFile = new RandomAccessFile(fileRef, "rw");
        for (int i = 0; i < 25*numBuckets; i++) {
            StringBuffer name = new StringBuffer();
            name.setLength(max1);
            indexFile.writeBytes(name.toString());
            indexFile.writeInt(-1);
        }

        return indexFile;
    }
}