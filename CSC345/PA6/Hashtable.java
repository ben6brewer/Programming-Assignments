/*
 * Author: Ben Brewer
 * Date: 4/24/23
 * File: Hashtable.java
 * Purpose: This class implements a hashtable using linear probing to handle collisions
 */

@SuppressWarnings("unchecked")
public class Hashtable<K, V> {
    private Pair[] table;
    private int n;//the number of key-value pairs in the table
    private int m;//the size of the table
    private double alphaHigh = 0.5;//resize if n/m exceeds this (1/2)
    private double alphaLow = 0.125;//resize if n/m goes below this (1/8)

    //constructor--default table size is 11
    public Hashtable() 
    {
        table = new Pair[11];
        n = 0;
        m = 11;
    }

    //constructor
    public Hashtable(int m) 
    {
        table = new Pair[m];
        n = 0;
        this.m = m;
    }

    /*
    * Method:     get
    * Purpose:    getter for value at a key
    * Parameters: K key - the key to search for
    * Returns:    V - the value at the key or null if not found
    */

    public V get(K key) 
    {
        V retval = null;
        int hashIndex = getIndexOfKey(key, table);
        while (table[hashIndex] != null)
        {
            if (table[hashIndex].getKey().equals(key))
            {
                retval = (V)table[hashIndex].getValue();
                return retval;
            }
            hashIndex = ( hashIndex + 1) % m;
        }
        return retval;
    }

    /*
    * Method:     put
    * Purpose:    setter for value at a key, adds a new pair if
    *             key is not found using linear probing to handle collisions
    * Parameters: K key - the key to search for or add
    *             V val - the value to set at the key
    * Returns:    V - the value at the key or null if not found
    */

    public void put(K key, V val) 
    {
        int hashIndex = getIndexOfKey(key, table);
        if (table[hashIndex] != null)
        {
            table[hashIndex].setValue(val);
            return;
        }
        else
        {
            Pair newPair = new Pair(key, val);
            table[hashIndex] = newPair;
            n++;
        }

        //resizing if necessary
        if((double)n/m > alphaHigh)
            resize(getNextNum(2*m));
    }

    /*
    * Method:     delete
    * Purpose:    deletes a pair at a key by setting boolean
    *             isDeleted to true
    * Parameters: K key - the key to delete at
    * Returns:    V - the value at the key or null if not found
    */

    public V delete(K key) 
    {
        V retval = null;
        int hashIndex = getIndexOfKey(key, table);
        if (table[hashIndex] != null)
        {
            retval = (V)table[hashIndex].getValue();
            table[hashIndex].setDeleted(true);
            n--;
        }

        //resizing if necessary
        if(m/2 >= 11 && (double)n/m < alphaLow)
            resize(getNextNum(m/2));
        return retval;
    }

    /*
    * Method:     isEmpty
    * Purpose:    checks if the table is empty
    * Parameters: none
    * Returns:    boolean - true if empty, false otherwise
    */

    public boolean isEmpty() 
    {
        return size() == 0;
    }

    /*
    * Method:     size
    * Purpose:    returns the number of key-value pairs in the table
    * Parameters: none
    * Returns:    int n - num of key-value pairs
    */

    public int size() 
    {
        return n;
    }

    //This method is used for testing only. Do not use this method yourself for any reason
    //other than debugging. Do not change this method.
    public Pair[] getTable() 
    {
	return table;
    }

    //PRIVATE

    //gets the next multiple of 6 plus or minus 1,
    //which has a decent probability of being prime.
    //Use this method when resizing the table.
    private int getNextNum(int num) 
    {
	if(num == 2 || num == 3)
	    return num;
	int rem = num % 6;
	switch(rem) {
	case 0: num++; break;
	case 2: num+=3; break;
	case 3: num+=2; break;
	case 4: num++; break;
	}
	return num;
    }

    /*
    * Method:     resize
    * Purpose:    resizes the table to a the new given size and clears the deleted elements
    * Parameters: int max - the new size of the table
    * Returns:    none
    */
    

    private void resize(int max) 
    {
        if (max > m)
            System.out.println("UP UP");
        else
            System.out.println("DOWN DOWN");
        m = max;
        Pair<K,V>[] temp = new Pair[max];
        for (int i = 0; i < table.length; i++)
        {
            // if a pair exists and is not deleted, add it to the new table
            if (table[i] != null && table[i].isDeleted() == false)
            {
                // rehash the key 
                int newIndex = getIndexOfKey((K) table[i].getKey(), temp);
                // put pair in new table
                while (temp[newIndex] != null)
                {
                    newIndex = (newIndex + 1) % m;
                }
                temp[newIndex] = table[i];
            }
        }
        table = temp;
    }

    /*
    * Method:     getIndexOfKey
    * Purpose:    getter for index of a key using linear probing to handle collisions
    * Parameters: K key - the key to get index of
    *             Pair[] table - the table to search in
    * Returns:    int hashIndex - the index of the key
    * Note:       Pair[] table parameter is necessary because resize() rehashes to a new table
    */

    private int getIndexOfKey(K key, Pair[] table)
    {
        // finds first index
        int hashIndex = key.hashCode() % m;
        if (hashIndex < 0)
        {
            hashIndex += m;
        }
        // handles linear probing 
        while (table[hashIndex] != null)
        {
            if (table[hashIndex].getKey().equals(key) && table[hashIndex].isDeleted() == false)
            {
                return hashIndex;
            }
            hashIndex = (hashIndex + 1) % m;
        }
        return hashIndex;
    }   
}