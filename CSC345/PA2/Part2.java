/*
 * Author : Ben Brewer
 * File   : Part2.java
 * Date   : 2/7/23
 * Purpose: Implement a method that sorts an array by by its absolute value 
 *          but not necessarily in ascending order. In the given array, negative
 *          numbers should come first followed by any number of 0's followed by
 *          positive numbers. Ex: {-3, -9, 0, 0, 1, 4, 1} is a sorting. The 
 *          method should access the array <= (4 * size) times.
 */
public class Part2
{
    /*
     * Method    : divideArray
     * Purpose   : sorts an array by by its sign but not necessarily in ascending order
     * Parameters: Array a - the given array
     * Returns   : Array a - the sorted array
     */
    public static Array divideArray(Array a) 
    {
        // create three pointers to track the index of positive, negative, and zero number index's
        // start the negative pointer at the beginning of the array where negative values belong
        int negativePointerIndex = 0;
        // start the zero pointer at the beginning of the array but is incremented
        int zeroPointerIndex = 0;
        // start the positive pointer at the end of the array where positive values belong
        int positivePointerIndex = a.length() - 1;
        // zero pointer should never go past positive number pointer else array is sorted
        while (zeroPointerIndex <= positivePointerIndex) 
        {
            int val = a.getVal(zeroPointerIndex);
            // if the value is negative, swap it with the value at the negative pointer
            if (val < 0) 
            {
                int tempVal = a.getVal(negativePointerIndex);
                a.setVal(negativePointerIndex, val);
                a.setVal(zeroPointerIndex, tempVal);
                negativePointerIndex++;
                zeroPointerIndex++;
            } 
            // if the value is 0, increment the zero pointer
            else if (val == 0) 
            {
                zeroPointerIndex++;
            } 
            // if the value is positive, swap it with the value at the positive pointer
            else 
            {
                int tempVal = a.getVal(positivePointerIndex);
                a.setVal(positivePointerIndex, val);
                a.setVal(zeroPointerIndex, tempVal);
                positivePointerIndex--;
            }
        }
        return a;
    }

    /*
     * Method    : divideArray2
     * Purpose   : sorts an array by by its sign but not necessarily in ascending order
     * Parameters: Array a - the given array
     * Returns   : Array a - the sorted array
     * 
     * NOTE: DISREGUARD METHODS BELOW
     * 
     * This method was my first implementation but it was not as efficient as the one above
     * because it was accessing the array more than (4 * size) times.
     */

    public static Array divideArray2(Array a) 
    {
        // these are counter variables to keep track of the number of elements
        // in the array that are negative, zero, and positive that still need to be sorted
        // Ex: Array of size 8 will have numNegatives + numZeroes + numPositives = 8 
        // because the array still needs to be checked and sorted
        int numNegatives = 0;
        int numZeroes = 0;
        int numPositives = 0;
        // count the number of negative, zero, and positive elements in the array
        for (int i = 0; i < a.length(); i++)
        {
            if (a.getVal(i) < 0)
            {
                numNegatives++;
            }
            else if (a.getVal(i) == 0)
            {
                numZeroes++;
            }
            else
            {
                numPositives++;
            }
        }
        int i = 0;
        // loops until all negative numbers are sorted
        while (numNegatives > 0)
        {
            // val is negative at index i
            if (a.getVal(i) < 0)
            {
                // decrement the number of negative numbers that still need to be sorted
                numNegatives--;
                i++;
            }
            // val is not negative at index i so it needs to be swapped with the next
            // negative number in the array. nextNegative() searches from left to right
            else
            {
                swap(a, i, nextNegative(a));
            }
        }
        // loops until all zero numbers are sorted
        while (numZeroes > 0)
        {
            // val is zero at index i
            if (a.getVal(i) == 0)
            {
                // decrement the number of zero numbers that still need to be sorted
                numZeroes--;
                i++;
            }
            // val is not zero at index i so it needs to be swapped with the next zero
            // number in the array
            else
            {
                swap(a, i, nextZero(a));
            }
        }
        // loops until all positive numbers are sorted
        while (numPositives > 0)
        {
            // val is positive at index i
            if (a.getVal(i) > 0)
            {
                // decrement the number of positive numbers that still need to be sorted
                numPositives--;
                i++;
            }
            // val is not positive at index i so it needs to be swapped with the next
            // positive number in the array. nextPositive() searches from right to left
            else
            {
                swap(a, i, nextPositive(a));
            }
        }
        return a;
    }

    /*
     * Method    : nextPositive
     * Purpose   : helper function for divideArray2 that finds the next positive
     *             number in the array by searching from right to left
     * Parameters: Array a - the given array
     * Returns   : int - the index of the next positive number in the array
     */

    private static int nextPositive(Array a)
    {
        for (int i = a.length() - 1; i > 0 ; i--)
        {
            if (a.getVal(i) > 0)
            {
                return i;
            }
        }
        return -1;
    }

    /*
     * Method    : nextZero
     * Purpose   : helper function for divideArray2 that finds the next zero
     *             number in the array
     * Parameters: Array a - the given array
     * Returns   : int - the index of the next zero number in the array
     */
    private static int nextZero(Array a)
    {
        for (int i = a.length() - 1; i > 0 ; i--)
        {
            if (a.getVal(i) == 0)
            {
                return i;
            }
        }
        return -1;
    }

    /*
     * Method    : nextNegative
     * Purpose   : helper function for divideArray2 that finds the next negative
     *             number in the array by searching from left to right
     * Parameters: Array a - the given array
     * Returns   : int - the index of the next negative number in the array
     */

    private static int nextNegative(Array a)
    {
        for (int i = 0; i < a.length() -1 ; i++)
        {
            if (a.getVal(i) < 0)
            {
                return i;
            }
        }
        return -1;
    }

    /*
     * Method    : swap
     * Purpose   : helper function for divideArray2 that swaps the index of two values
     * Parameters: Array a - the given array
     *             int i - the index of the first value
     *             int j - the index of the second value
     * Returns   : void
     */

    private static void swap(Array a , int i, int j)
    {
        int temp = a.getVal(i);
        a.setVal(i, a.getVal(j));
        a.setVal(j, temp);
    }
}