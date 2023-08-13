/*
 * Author: Ben Brewer
 * Date: 4/3/23
 * File: Merge.java
 * Purpose: This class is responsible for sorting an array using merge sort iteratively
 */

public class Merge
{
    /*
    * Method: sort
    * Purpose: sorts an array using merge sort iteratively
    * Parameters: Array A - the array to be sorted
    * Returns: void
    */
    public static void sort(Array A)
    {
        // implements merge sort using iteration
        int n = A.length();
        A.initExtra(n);
        for (int i = 1; i < n; i *= 2)
        {
            for (int j = 0; j < n - i; j += 2 * i)
            {
                // break this up into multiple lines for readability
                int left = j;
                int right = j + i;
                int end = Math.min(j + 2 * i, n);
                merge(A, left, right, end);
            }
        }
    }

    /*
    * Method: merge
    * Purpose: merges two sorted arrays into one sorted array
    * Parameters: Array A - the array to be sorted
    *             int[] temp - the temporary array to be used
    *             int left - the left index of the first array
    *             int right - the right index of the first array
    *             int end - the right index of the second array
    * Returns: void
    */

    private static void merge(Array A, int left, int right, int end)
    {
        int i = left;
        int j = right;
        int k = left;
        // merge the two arrays into the temporary array
        while (i < right && j < end)
        {
            if (A.getVal(i) < A.getVal(j))
            {
                A.setExtraVal(k, A.getVal(i));
                i++;
            }
            else
            {
                A.setExtraVal(k, A.getVal(j));
                j++;
            }
            k++;
        }
        while (i < right)
        {
            A.setExtraVal(k, A.getVal(i));
            i++;
            k++;
        }
        while (j < end)
        {
            A.setExtraVal(k, A.getVal(j));
            j++;
            k++;
        }
        // copy the sorted array back into the original array
        for (int l = left; l < end; l++)
        {
            A.setVal(l, A.getExtraVal(l));
        }
    }
}