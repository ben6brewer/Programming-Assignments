/*
 * Author : Ben Brewer
 * File   : Part1.java
 * Date   : 2/7/23
 * Purpose: Implement a method that finds the maximum sum of m consecutive 
 *          elements in an integer array in <= (2 * size) accesses of the array.
 */

 public class Part1 
 {    
    /*
     * Method    : maxSum
     * Purpose   : finds the maximum sum of m consecutive elements in an integer array
     *             in <= (2 * size) accesses of the array.
     * Parameters: Array a - the given array
     *             int m - the number of consecutive elements to calculate sum
     * Returns   : int maxSum - the maximum sum of m consecutive elements in the array
     */
    
    public static int maxSum(Array a, int m) 
    {
        int maxSum = 0;
        int currSum = 0;
        // calculate the sum of the first m elements
        for (int i = 0; i < m; i++) {
            currSum += a.getVal(i);
        }
        // set the maxSum to the initial sum of first m elements
        maxSum = currSum;
        // start at m and go to the end of the array
        for (int i = m; i < a.length(); i++) 
        {
            // subtract the value at i-m and add the value at i
            currSum = currSum - a.getVal(i-m) + a.getVal(i);
            maxSum = Math.max(maxSum, currSum);
        }
        return maxSum;
    }
 }
