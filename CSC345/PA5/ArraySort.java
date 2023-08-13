/*
 * Author: Ben Brewer
 * Date: 4/3/23
 * File: ArraySort.java
 * Purpose: This class is responsible for sorting an array based on the input. The method of 
 *          sorting is determined by the input's size and if it is locality-aware.
 */

 public class ArraySort 
 {
    /*
    * Method: sort
    * Purpose: sorts an array based on the input
    * Parameters: Array array - the array to be sorted
    *             int num - the number of the test case
    *             int d - locality-awareness
    * Returns: void
    */
 
     public static void sort(Array array, int num, int d) 
     {
        // t1: DESCENDING is GREAT (3.5/3.5)
        //     The input array is in descending order.
        if (num == 1)
            descendingSort(array);
        
        // t2: QUICKSORT is GREAT (3.5/3.5)
        //     The input array is in random order.
        else if (num == 2)
            quicksort(array, 0, array.length() - 1);
        
        // t3: INSERTION is GOOD (3.25/3.5)
        //     The input array is almost sorted, meaning that only a few items are out of order.
        else if (num == 3)
            insertionsort(array);

        // t4: INSERTION is GREAT (3.5/3.5)
        //     Array is locality-aware. That means that every item in the input array is
        //     no more than d positions away from where it will end up in the sorted array.
        else if (num == 4)
            insertionsort(array);

        // t5: SHELL is OK (3/3.5)
        //     The input array is mostly sorted with just a small number of unsorted elements at the end.
        else if (num == 5)
            shellSort(array);

        // t6: MERGE is TOO HIGH (2.5/3.5)
        //     The input array is made up of a small number of sorted subarrays.
        else if (num == 6)
            mergeSortIterative(array);

        // t7: BIN is OK (3/3.5)
        //     The input array is random, but the values in the array are relatively limited in range.
        else if (num == 7)
            binSort(array);
    }

    public static void mergeSortIterative(Array A)
    {
        // add two subsorted arrays to the queue
        Queue<Integer> q = new Queue<Integer>();
        int count = 0;
        for (int i = 0; i < A.length(); i ++)
        {
            if (A.getVal(i) <= A.getVal(i + 1) && count <= 1)
            {
                q.enqueue(A.getVal(i));
            }
            else if (A.getVal(i) > A.getVal(i + 1))
            {
                count++;
            }
            if (count == 2)
            {
                count = 0;
                mergeTwoArrays(A, q);
            }
        }
    }

    /*
     * Method: mergeTwoArrays
     * Purpose: merges two sorted arrays into one sorted array and stores it in the passed in array
     * Parameters: Array A - the array to be sorted
     *             int left - the start index of the first array
     *             int right - the end index of the first array
     *             int left2 - the start index of the second array
     *             int right2 - the end index of the second array
     * Returns: void
     */

    private static void mergeTwoArrays(Array A, Queue q)
    {
        // pop all items from queue and add to temp array
        int tempArrSize = q.size();
        A.initExtra(tempArrSize);
        for (int i = 0; i < q.size(); i++)
        {
            A.setExtraVal(i, q.dequeue());
        }
        // sort the temp array
        for (int i = 0; i < tempArrSize; i++)
        {
            for (int j = i + 1; j < tempArrSize; j++)
            {
                if (A.getExtraVal(i) > A.getExtraVal(j))
                {
                    int temp = A.getExtraVal(i);
                    A.setExtraVal(i, A.getExtraVal(j));
                    A.setExtraVal(j, temp);
                }
            }
        }
    }

    /*
    * Method: getSubArrayIndex
    * Purpose: finds the start and end index of the next subsorted array
    * Parameters: Array array - the array to be searched
    * Returns: int - the length of the smallest subarray
    */

    private static int getSubArrayIndex(Array array, int start)
    {
        int n = array.length();
        int end = start + 1;
        while (end < n && array.getVal(end) >= array.getVal(end - 1))
            end++;
        return end;
    }


 
     
    /*
    * Method: descendingSort
    * Purpose: reverse an array to have it sorted in ascending order
    * Parameters: Array array - array to be sorted
    * Returns: void
    */

    private static void descendingSort(Array array)
    {
        int n = array.length();
        for (int i = 0; i < n / 2; i++) 
        {
            // next index to be swapped
            int temp = (n - i) - 1;
            array.swap(i, temp);
        }
    }
 
    /*
    * Method: binSort
    * Purpose: sorts an array using bins
    * Parameters: Array array - the array to be sorted
    * Returns: void
    */

    private static void binSort(Array array) 
    {
        int n = array.length();
        int max = array.getVal(0);
        int min = array.getVal(0);
        for (int i = 1; i < n; i++) {
            if (array.getVal(i) > max)
                max = array.getVal(i);
            if (array.getVal(i) < min)
                min = array.getVal(i);
        }
        int range = max - min + 1;
        array.initExtra(range);
        for (int i = 0; i < n; i++) {
            array.setExtraVal(array.getVal(i) - min, array.getExtraVal(array.getVal(i) - min) + 1);
        }
        int index = 0;
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < array.getExtraVal(i); j++) {
                array.setVal(index, i + min);
                index++;
            }
        }
    }
     
    /*
    * Method: mergeSort
    * Purpose: sorts an array using merge sort
    * Parameters: Array array - the array to be sorted
    *            int low - the low index of the array
    *            int high - the high index of the array
    * Returns: void
    */
 
    private static void mergeSort(Array array) {
        int n = array.length();
        array.initExtra(n);
        mergeSort(array, 0, n - 1);
    }
 
     /*
      * Method: mergeSort
      * Purpose: overwritten method for merge sort if parameters are passed in
      * Parameters: Array array - the array to be sorted
      *           int left - the left index of the array
      *           int right - the right index of the array
     */
 
    private static void mergeSort(Array array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    /*
    * Method: merge
    * Purpose: helper method for merge sort used to group arrays
    * Parameters: Array array - the array to be sorted
    *          int left - the left index of the array
    *          int mid - the middle index of the array
    *          int right - the right index of the array
    * Returns: void
    */
 
    private static void merge(Array array, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            if (array.getVal(i) < array.getVal(j)) {
                array.setExtraVal(k, array.getVal(i));
                i++;
            } else {
                array.setExtraVal(k, array.getVal(j));
                j++;
            }
            k++;
        }
        while (i <= mid) {
            array.setExtraVal(k, array.getVal(i));
            i++;
            k++;
        }
        while (j <= right) {
            array.setExtraVal(k, array.getVal(j));
            j++;
            k++;
        }
        for (int l = left; l <= right; l++) {
            array.setVal(l, array.getExtraVal(l));
        }
    }
 

 
    

    /*
    * Method: shellSort 
    * Purpose: sorts an array using shell sort 
    * Parameters: Array array - the array to be sorted
    * Returns: void
    */

    private static void shellSort(Array array) 
    {
        int n = array.length();
        for (int gap = n / 2; gap > 0; gap /= 2) 
        {
            for (int i = gap; i < n; i++) 
            {
                int temp = array.getVal(i);
                int j;
                for (j = i; j >= gap && array.getVal(j - gap) > temp; j -= gap) 
                {
                    array.setVal(j, array.getVal(j - gap));
                }
                array.setVal(j, temp);
            }
        }
    }
 
    /*
    * Method: insertionsort 
    * Purpose: sorts an array using insertion sort 
    * Parameters: Array array - the array to be sorted
    * Returns: void
    */

    private static void insertionsort(Array array) 
    {
        int n = array.length();
        for (int i = 1; i < n; i++) {
            int val = array.getVal(i);
            int j = i;
            while (j > 0 && array.getVal(j - 1) > val) 
            {
                array.setVal(j, array.getVal(j - 1));
                j--;
            }
            array.setVal(j, val);
        }
    }
 
    /*
    * Method: quicksort 
    * Purpose: sorts an array using quicksort 
    * Parameters: Array array - the array to be sorted
    *             int left - the left index of the array
    *             int right - the right index of the array
    * Returns: void
    */

    private static void quicksort(Array array, int left, int right) 
    {
        if (left < right) 
        {
            int pivot = partition(array, left, right);
            quicksort(array, left, pivot - 1);
            quicksort(array, pivot + 1, right);
        }
    }

    /*
    * Method: partition 
    * Purpose: helper method for quicksort. Partitions an array based on a pivot index
    * Parameters: Array array - the array to be sorted
    *             int left - the left index of the array
    *             int right - the right index of the array
    * Returns: int - the index of the pivot
    */

    private static int partition(Array array, int left, int right) 
    {
        int pivot = array.getVal(right);
        int i = left - 1;
        for (int j = left; j < right; j++) 
        {
            if (array.getVal(j) <= pivot) 
            {
                i++;
                int temp = array.getVal(i);
                array.setVal(i, array.getVal(j));
                array.setVal(j, temp);
            }
        }
        int temp = array.getVal(i + 1);
        array.setVal(i + 1, array.getVal(right));
        array.setVal(right, temp);
        return i + 1;
    }
}