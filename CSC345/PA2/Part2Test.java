public class Part2Test 
{
    public static int score;
    public static void main(String[] args)
    {
        testArr(8);
        System.out.println();
        testArr(16);
        System.out.println();
        testArr(32);
        System.out.println();
        testArr(64);
        System.out.println();
        testArr(128);
        System.out.println();
        testArr(256);
        System.out.println();
        testArr(512);
        System.out.println();
        testArr(1024);
        System.out.println();
        testArr(2048);
        System.out.println();
        testArr(4096);

        System.out.println();
        System.out.println("Assuming the algo is working correctly (scroll up to check first two cases)");
        System.out.println("Estimated Score: " + ((score + 10) * 100) / 30 + "%");
        System.out.println();
        System.out.println();
    }

    private static void testArr(int size)
    {
        Array a = createRandomArray(size);
        System.out.println("Testing size: " + size);
        a = Part2.divideArray(a);
        if (size <= 16)
        {
            System.out.println("Unsorted Array: " + a);
            System.out.println("Sorted Array : " + a);
        }
        if (a.getAccessCount()> (4 * size))
        {
            System.out.println("Access count is too high");
            System.out.println("Expected: " + (4 * size));
            System.out.println("Actual: " + a.getAccessCount());
            System.out.println("Over by " + ((a.getAccessCount()/ (4 * size)) * 100) + "%");
            System.out.println("---------------------------------------");
        }
        else
        {
            score += 2;
            System.out.println("Passed test case"); 
            System.out.println("Expected: " + (4 * size));
            System.out.println("Actual: " + a.getAccessCount());
            System.out.println("---------------------------------------");
        }
    }

    // create a random array of size <size> using the Array.java class
    private static Array createRandomArray(int size)
    {
        Array a = new Array(size);
        for(int i = 0; i < size; i++)
        {
            int max = 10;
            a.setValue(i, (int)(Math.random() * max) - (max / 2));
        }
        return a;
    }   
}
    