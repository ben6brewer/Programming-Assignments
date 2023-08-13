/*
* Author: Ben Brewer, Kareem Khalidi, and Joey Mauriello
* File: MaxPQ.java
* Date: 4/11/2023
* Purpose: Implement a Max Priority Queue to be used in Huffman Encoding
*/

import java.util.HashMap;

public class MaxPriorityQueue <Key extends Comparable<Key>>
{
    private char[] pq;
    private int size = 0;
    private int initialCapacity;

    // creates a new MaxPQ with a default capacity of 10
    public MaxPriorityQueue()
    {
        pq = new char[10];
        initialCapacity = 10;
    }

    // constructor: creates a new MaxPQ with a capacity of the parameter cap
    public MaxPriorityQueue(int cap)
    {
        pq = new char[cap];
        initialCapacity = cap;
    }

    /*
    * Method: insert
    * Purpose: inserts a new patient into the MaxPQ
    * Parameters: Patient item - the patient to be inserted
    * Returns: void
    */

    public void insert(char item)
    {
    // best: O(logN), worst: O(N)--for resizing, amortized: O(logN)
        if (pq.length == size)
        {
            resize(2 * size);
        }
        pq[size] = item;
        swim(size);
        size++;
    }

    /*
    * Method: delMax
    * Purpose: deletes and returns the patient with the highest priority
    * Parameters: none
    * Returns: Patient - the patient with the highest priority
    */

    public char delMax() throws EmptyQueueException
    {
        // best: O(logN), worst: O(N)--for resizing amortized: O(logN)
        if (isEmpty())
        {
            throw new EmptyQueueException();
        }
        char retval = pq[0];
        pq[0] = pq[size - 1];
        sink(0);
        size--;
        if ((size < (pq.length / 4)) && ((pq.length / 2) > initialCapacity))
        {
            resize(pq.length / 2);
        }
        return retval;
    }

    /*
    * Method: getMax
    * Purpose: returns the patient with the highest priority without deleting or returning it
    * Parameters: none
    * Returns: Patient - the patient with the highest priority
    */

    public char getMax() throws EmptyQueueException
    {
        if (isEmpty())
        {
            throw new EmptyQueueException();
        }
        return pq[0];
    }

    /*
    * Method: size
    * Purpose: returns the number of patients in the MaxPQ
    * Parameters: none
    * Returns: int - the number of patients in the MaxPQ
    */

    public int size()
    {
        return size;
    }

    /*
    * Method: isEmpty
    * Purpose: returns true if the MaxPQ is empty, false otherwise
    * Parameters: none
    * Returns: boolean - true if the MaxPQ is empty, false otherwise
    */

    public boolean isEmpty()
    {
        if (size == 0)
        {
            return true;
        }
        return false;
    }

    /*
    * Method: resize
    * Purpose: resizes the MaxPQ to the capacity of the parameter
    * Parameters: int capacity - the new capacity of the MaxPQ
    * Returns: void
    */

    private void resize(int capacity)
    {
        char[] tempPQ = new char[capacity];
        for (int i = 0; i < size; i++)
        {
            tempPQ[i] = pq[i];
        }
        pq = tempPQ;
    }

    /*
    * Method: swim
    * Purpose: bubbles the patient at the index i up the heap until it is in the correct position
    * Parameters: int i - the index of the patient to be bubbled up
    * Returns: void
    */


    private void swim(int i)
    {
        HashMap<Character, Integer> charFreq = HuffmanEncoding.getCharFrequency("RandomText.txt");
        while (i > 0 && charFreq.get(pq[i]) > charFreq.get(pq[parent(i)]))
        {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /*
    * Method: sink
    * Purpose: bubbles the patient at the index i down the heap until it is in the correct position
    * Parameters: int i - the index of the patient to be bubbled down
    * Returns: void
    */

    private void sink(int i)
    {
        HashMap<Character, Integer> charFreq = HuffmanEncoding.getCharFrequency("RandomText.txt");
        int largestChild = i;
        int leftChild = leftChild(i);
        int rightChild = rightChild(i);
        if (leftChild(i) < size)
        {
            if (charFreq.get(pq[leftChild]) > charFreq.get(pq[largestChild]))
            {
                largestChild = leftChild(i);
            }
            if (rightChild(i) < size)
            {
                if (charFreq.get(pq[rightChild]) > charFreq.get(pq[largestChild]))
                {
                    largestChild = rightChild(i);
                }
            }
            if (largestChild != i)
            {
                swap(i, largestChild);
                sink(largestChild);
            }
        }
    }

    /*
    * Method: swap
    * Purpose: swaps the patients at the indices i and j
    * Parameters: int i - the index of the first patient to be swapped
    *             int j - the index of the second patient to be swapped
    * Returns: void
    */

    private void swap(int i, int j)
    {
        char temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    /*
    * Method: parent
    * Purpose: returns the index of the parent of the patient at the index i
    * Parameters: int i - the index of the patient whose parent is to be returned
    * Returns: int - the index of the parent of the patient at the index i
    */

    private int parent(int i)
    {
        return (i - 1) / 2;
    }

    /*
    * Method: leftChild
    * Purpose: returns the index of the left child of the patient at the index i
    * Parameters: int i - the index of the patient whose left child is to be returned
    * Returns: int - the index of the left child of the patient at the index i
    */

    private int leftChild(int i)
    {
        return 2 * i + 1;
    }

    /*
    * Method: rightChild
    * Purpose: returns the index of the right child of the patient at the index i
    * Parameters: int i - the index of the patient whose right child is to be returned
    * Returns: int - the index of the right child of the patient at the index i
    */

    private int rightChild(int i)
    {
        return 2 * i + 2;
    }
    
    private int compareTo(char other)
    {
        HashMap<Character, Integer> charFreq = HuffmanEncoding.getCharFrequency("RandomText.txt");
        if (charFreq.get(this) > charFreq.get(other))
        {
            return 1;
        }
        else if (charFreq.get(this) < charFreq.get(other))
        {
            return -1;
        }
        return 0;
    }
}