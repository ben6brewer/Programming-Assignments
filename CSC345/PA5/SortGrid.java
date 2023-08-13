/*
 * Author: Ben Brewer
 * Date: 4/3/23
 * File: SortGrid.java
 * Purpose: This class is responsible for sorting a grid based on a given definition differentiated
 *          by the method call sortA or sortB.
 */

public class SortGrid
{
    /*
    * Method: sortA
    * Purpose: Sorts the grid using an algorithm where, if you laid the rows side by side,
    *          the resulting array would be sorted.
    * Parameters: Grid g - the grid to be sorted
    * Returns: void
    */

    public static void sortA(Grid g) 
    {
        int size = g.size() * g.size();
        // heapify
        for (int i = Math.floorDiv(size, 2); i >= 0; i--)
        {
            sink(i, size, g);
        }
        // sink down
        for (int i = size - 1; i >= 0; i--)
        {
            g.swap(0, 0, i / g.size(), i % g.size());
            sink(0, i, g);
        }
    }

    /*
     * Method: getNum
     * Purpose: Gets the number at a given index in the grid using getLoc
     * Parameters: int index - the index of the number to be returned
     *             Grid g - the grid to be searched
     */
    private static int getNum(int index, Grid g)
    {
        int column = g.size();
        Loc locAtIndex = g.getLoc(index / column, index % column);
        return locAtIndex.getIntVal();
    }
    

    /*
     * Method: leftChild
     * Purpose: Gets the index of the left child at the given parent
     * Parameters: int i - the index of the parent
     * Returns: int - the index of the left child
     */

    private static int leftChild(int i)
    {
        return 2 * i + 1;
    }

    /*
     * Method: rightChild
     * Purpose: Gets the index of the right child at the given parent
     * Parameters: int i - the index of the parent
     * Returns: int - the index of the right child
     */

    private static int rightChild(int i)
    {
        return 2 * i + 2;
    }

    /*
     * Method: sink
     * Purpose: sorts the heap at the index to its correct position down the heap
     * Parameters: int i - the index of the element to be sunk
     *             int size - the size of the heap
     *             Grid g - the grid to be sorted
     * Returns: void
     */

    private static void sink(int i, int size, Grid g)
    {
        int largestChild = i;
        if (leftChild(i) < size && getNum(leftChild(i), g) > (getNum(largestChild, g)))
        {
            largestChild = leftChild(i);
        }
        if (rightChild(i) < size && getNum(rightChild(i), g) > (getNum(largestChild, g)))
        {
            largestChild = rightChild(i);
        }
        if (largestChild != i)
        {
            g.swap(i / g.size(), i % g.size(), largestChild / g.size(), largestChild % g.size());
            sink(largestChild, size, g);
        }
    }
    
    /*
     * Method: sortB
     * Purpose: Sorts the grid using an algorithm where, each element is less than
     *          or equal to the elements directly to the right and directly down from itself.
     * Parameters: Grid g - the grid to be sorted
     * Returns: void
     */

    public static void sortB(Grid g)
    {
        // sort each row
        for (int i = 0; i < g.size(); i++)
        {
            sortRow(g, i);
        } 

        // set the row in the temp grid to be the column in the original grid
        for (int i = 0; i < g.size(); i++)
        {
            for (int j = 0; j < g.size(); j++)
            {
                g.setTempVal(i, j, g.getIntVal(j, i));
            }
        }

        //copy over elements from temp grid to grid
        for (int i = 0; i < g.size(); i++)
        {
            for (int j = 0; j < g.size(); j++)
            {
                g.setIntVal(i, j, g.getTempVal(i, j));
            }
        }
        
        // re sort the new rows
        for (int i = 0; i < g.size(); i++)
        {
            sortRow(g, i);
        }
    }

    /*
     * Method: sortRow
     * Purpose: Sorts a row of the grid using selection sort
     * Parameters: Grid g - the grid to be sorted
     *             int row - the row to be sorted
     * Returns: void
     */

    private static void sortRow(Grid g, int row)
    {
        int size = g.size();
        for (int i = 0; i < size; i++) 
        {
            int min = g.getIntVal(row, i);
            int minI = i;
            for (int j = i; j < size; j++) 
            {
                if (g.getIntVal(row, j) < min) 
                {
                    min = g.getIntVal(row, j);
                    minI = j;
                }
            }
            g.swap(row, i, row, minI);
        }
    }
}