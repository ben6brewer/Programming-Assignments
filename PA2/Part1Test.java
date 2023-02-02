public class Part1Test 
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
        System.out.println("Estimated Score: " + (score * 100) / 20 + "%");

    }
    private static void testArr(int size)
    {
        Array a = createRandomArray(size);
        System.out.println("Before access count: " + a.getAccessCount());
        System.out.println("Testing size: " + size);
        int maxSum = Part1.maxSum(a, 3);
        if (maxSum != GetLargestSum(a, 3))
        {
            System.out.println("Incorrect sum");
            System.out.println("Correct sum: " + GetLargestSum(a, 3));
            System.out.println("Actual Sum: " + Part1.maxSum(a, 3));
        } 
        else if (maxSum == GetLargestSum(a, 3))
        {
            System.out.println("Sum is correct");
            score++;
        }
        if (a.getAccessCount() > (2 * size))
        {
            System.out.println("Access count is too high");
            System.out.println("Expected: " + (2 * size));
            System.out.println("Actual: " + a.getAccessCount());
            System.out.println("Over by " + ((a.getAccessCount() / (2 * size)) * 100) + "%");
            System.out.println("---------------------------------------");
        }
        else
        {
            score++;
            System.out.println("Passed test case");
            System.out.println("Expected: " + (2 * size));
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

    static int GetLargestSum(Array a, int n)
    {
        int largestSum = 0;
        int previousSum = 0;

        for (int i = 0; i <= a.length() - n; i++)
        {
            if (i == 0)
            {
                for (int j = 0; j < n; j++)
                {
                    largestSum += a.getValue(j);
                }

                previousSum = largestSum;
            }
            else
            {
                int currentSum = previousSum - a.getValue(i - 1) + a.getValue(i + n - 1);
                if (currentSum > largestSum)
                {
                    largestSum = currentSum;
                }
                previousSum = currentSum;
            }
        }
        return largestSum;
    }
}