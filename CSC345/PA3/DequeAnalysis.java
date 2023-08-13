/*
 * Author : Ben Brewer
 * File   : DequeAnalysis.java
 * Date   : 2/14/2023
 * Purpose: performs experiments on the two Deques in order to compare
 *          their overall performance. The goal is to compare the results
 *          of the different resizing strategies–particularly as the number
 *          of operations gets larger.
 */

public class DequeAnalysis 
{
    public static void main(String[] args) 
    {
        // construct a Deque1 and a Deque2 of size 1
        Deque1<Integer> deque1 = new Deque1(1);
        Deque2<Integer> deque2 = new Deque2(1);

        // graph 1 data printed in console
        graph1Deque1(deque1);
        graph1Deque2(deque2);
        resetDeques(deque1, deque2);

        // graph 2 and 3 data printed in console
        graph2and3(deque1, deque2);
        resetDeques(deque1, deque2);
    }

    /*
     * Method    : graph1Deque1
     * Purpose   : performs a bunch of enqueues on Deque1 and gets
     *             the current access count after each iteration.
     * Parameters: Deque1<Integer> deque1 - the first deque
     * Returns   : void
     */

     public static void graph1Deque1(Deque1<Integer> deque1)
     {
         // initialize a 2D array of cords to graph later
         int[][] dequeCords = new int[1001][2];
 
         for (int i = 0; i < 1001; i++)
         {
             int randomNumber = 1;
             deque1.enqueue(randomNumber);
             dequeCords[i][0] = i;
             dequeCords[i][1] = deque1.getAccessCount();
         }
 
         // prints out each point in the 2D array
         System.out.println("deque1 Coordinates: ");
         printArr(dequeCords);
     }  
     
     /*
      * Method    : graph1Deque2
      * Purpose   : performs a bunch of enqueues on Deque2 and gets
      *             the current access count after each iteration.
      * Parameters: Deque2<Integer> deque1 - the first deque
      * Returns   : void
      */
 
      public static void graph1Deque2(Deque2<Integer> deque2)
      {
          // initialize a 2D array of cords to graph later
          int[][] dequeCords = new int[1001][2];
  
          for (int i = 0; i < 1001; i++)
          {
             int randomNumber = 1;
             deque2.enqueue(randomNumber);
             dequeCords[i][0] = i;
             dequeCords[i][1] = deque2.getAccessCount();
          }
  
          // prints out each point in the 2D array
          System.out.println("deque2 Coordinates: ");
          printArr(dequeCords);
      }

    /*
     * Method    : graph2and3
     * Purpose   : performs a bunch of enqueues on a Deque1 and a Deque2 and gets and resets
     *             the access count after each iteration. I combined these two methods as I thought it
     *             would be easier because they both use the same code and the only difference is the 
     *             Deque1 and Deque2 objects being tested.
     * Parameters: Deque1<Integer> deque1 - the first deque
     *             Deque2<Integer> deque2 - the second deque
     * Returns   : void
     */

    public static void graph2and3(Deque1<Integer> deque1, Deque2<Integer> deque2)
    {
        // initialize a 2D array of cords to graph later (1001 x, y coordinates)
        int[][] deque1Cords = new int[1001][2];
        int[][] deque2Cords = new int[1001][2];

        for (int i = 0; i < 1001; i++)
        {
            int randomNumber = 1;
            deque1.enqueue(randomNumber);
            deque2.enqueue(randomNumber);

            // store the access count as the y value and the number of enqueues as the x value
            deque1Cords[i][0] = i;
            deque1Cords[i][1] = deque1.getAccessCount();
            deque2Cords[i][0] = i;
            deque2Cords[i][1] = deque2.getAccessCount();

            deque1.resetAccessCount();
            deque2.resetAccessCount();
        }
        // prints out each point in the 2D array
        System.out.println("Graph 2 Deque1 Coordinates: ");
        printArr(deque1Cords);
        System.out.println("Graph 3 Deque2 Coordinates: ");
        printArr(deque2Cords);
    }

    /*
     * Method    : printArr
     * Purpose   : method to print the 2D array of coordinates for visualization and debugging
     * Parameters: int[][] arr - the 2D array of coordinates
     * Returns   : None
     */

    public static void printArr(int[][] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            System.out.println("x: " + arr[i][0] + " y: " + arr[i][1]);
        }
    }

    /*
     * Method    : resetDeques
     * Purpose   : helper function to reset the access count and the deques 
     *             to their original state of size 1
     * Parameters: Deque1<Integer> deque1 - the first deque
     *             Deque2<Integer> deque2 - the second deque
     * Returns   : None
     */

    public static void resetDeques(Deque1<Integer> deque1, Deque2<Integer> deque2)
    {
        // reset access count
        deque1.resetAccessCount();
        deque2.resetAccessCount();
        deque1 = new Deque1(1);
        deque2 = new Deque2(1);
    }
}
