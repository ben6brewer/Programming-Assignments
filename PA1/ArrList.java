/*
* Author: Ben Brewer
* File: ArrList.java
* Date: 1/24/2023
* Purpose: This class implements an ArrList using an array.
*          We were given an inefficient Array.java class to
*          optimize.
*/

class ArrList {
	private Array array;//the underlying array
	private int first;//the index of the first element in the list
	private int last;//the index of the first open position after the last element
	private int size;//the number of elements in the list
 
	//constructor: construct an ArrList with a starting capacity of 1
	ArrList()
	{
		this.array = new Array(1);
		this.first = 0;
		this.last = 0;
		this.size = 0;
	}
 
 
	// Method   : addLast
	// Purpose  : adds a new element (num) to the end of the ArrList
	// Params   : int num - the number to be added to the ArrList
	// Return   : void
	void addLast(int num)
	{
		if(size == array.length())
		{
			resize(2 * array.length());
		}
		array.setVal(last, num);
		last++;
		size++;
	}
 
 
	// Method   : addFirst
	// Purpose  : adds a new element (num) to the beginning of the ArrList
	// Params   : int num - the number to be added to the ArrList
	// Return   : void
	void addFirst(int num)
	{
		if(size == array.length())
		{
			resize(2 * array.length());
		}
		shiftRight(first);
		array.setVal(first, num);
		last++;
		size++;
	}
 
 
	// Method   : add
	// Purpose  : adds a new element (num) to the ArrList a specific index
	// Params   : int i - the index to add the element at
	//          : int num - the number to be added to the ArrList
	// Return   : void
	void add(int i, int num) throws ArrayIndexOutOfBoundsException
	{
		if(i < 0 || i >= size)
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		if (size == array.length()) 
		{
			resize(2 * array.length());
		}
		shiftRight(i);
		array.setVal(i, num);
		last++;
		size++;
	}
 
 
	// Method   : get
	// Purpose  : gets the element at a specific index
	// Params   : int i - the index to get the element at
	// Return   : int array.getVal(i) - the element at the index
	int get(int i) throws ArrayIndexOutOfBoundsException
	{
		if(i < 0 || i >= size)
			throw new ArrayIndexOutOfBoundsException();
		return array.getVal(i);
	}
 
	// Method   : indexOf
	// Purpose  : gets the first index of a specific element
	// Params   : int num - the element to find the first index of
	// Return   : int i - the first index of the element
	//          : int -1 - if the element is not in the ArrList
	int indexOf(int num)
	{
		int i = first;
		while(i < size)
		{
			if(array.getVal(i) == num)
				return i;
			i++;
		}
		return -1;
	}
 
 
	// Method   : contains
	// Purpose  : checks if the ArrList contains a specific element
	// Params   : int num - the element to check if it is in the ArrList
	// Return   : boolean true - if the element is in the ArrList
	//          : boolean false - if the element is not in the ArrList
	boolean contains(int num)
	{
		int index = this.indexOf(num);
		return index != -1;
	}
 
	// Method   : isEmpty
	// Purpose  : checks if the ArrList is empty
	// Params   : none
	// Return   : boolean true - if the ArrList is empty
	//          : boolean false - if the ArrList is not empty
	
	boolean isEmpty()
	{
		return size == 0;
	}
 
 
	// Method   : lastIndexOf
	// Purpose  : gets the last index of a specific element
	// Params   : int num - the element to find the last index of
	// Return   : int i - the last index of the element
	//          : int -1 - if the element is not in the ArrList
	int lastIndexOf(int num)
	{
		int i = last - 1;
		while(i >= 0) {
			if(array.getVal(i) == num)
				return i;
			i--;
		}
		return -1;
	}
 
 
	// Method   : removeFirst
	// Purpose  : removes and returns the first element
	// Params   : none
	// Return   : int num - the first element in the ArrList
	int removeFirst() throws EmptyListException
	{
		if(isEmpty())
			throw new EmptyListException();
		int num = array.getVal(first);
		// first = (first + 1) % array.length();
		// this.array = this.array.copyOfRange(1, size);
		shiftLeft(first + 1, 1);
		size--;
		last--;
		if (size < array.length() / 4)
			resize(array.length() / 2);
		return num;
	}
	/*
	 * int removeFirst() throws EmptyListException
   {
	   if(isEmpty())
		   throw new EmptyListException();
	   int num = array.getVal(first);
	   shiftLeft(first + 1, 1);
	   decrement(size, 1);
	   decrement(last, 1);
	   return num;
   }
	 */
 
 
	// Method   : removeLast
	// Purpose  : removes and returns the last element
	// Params   : none
	// Return   : int num - the last element in the ArrList
	int removeLast() throws EmptyListException
	{
		if(isEmpty())
			throw new EmptyListException();
		last--;
		int num = array.getVal(last);
		size--;
		return num;
	}
 
 
	// Method   : removeByIndex
	// Purpose  : removes and returns the element at a specific index
	// Params   : int i - the index to remove the element at
	// Return   : int num - the element at the index
	//          : int -9999 - if the index is out of bounds (dummy value)
	
	int removeByIndex(int i) throws EmptyListException
	{
		if (isEmpty())
			throw new EmptyListException();
		int num = -9999;
		if(i < 0 || i >= size)
		{
			return num;
		}
		if (i == 0)
			return removeFirst();
		else if (i == size - 1)
			return removeLast();
		else
		{
			num = array.getVal(i);
			shiftLeft(increment(i), 1);
		}
		size--;
		last--;
		return num;
	}
	   
	// Method   : removeRange
	// Purpose  : removes a range of elements from the ArrList
	// Params   : int i - the index to start removing elements at (inclusive)
	//          : int j - the index to stop removing elements at (inclusive)
	// Return   : void
	
	void removeRange(int i, int j) throws EmptyListException
	{
		if(isEmpty())
			throw new EmptyListException();
		if(i < 0 || i > size - 1 || j < 0 || j > size - 1)
			return;
		// if i > j, swap them to make i < j
		if(i > j)
		{
			int temp = i;
			i = j;
			j = temp;
		}
		shiftLeft(j, j-i+1);
		size -= j-i+1;
		last -= j-i+1;
	}
	
	
 
 
	// Method   : removeByValue
	// Purpose  : remove the first occurrence of num from the ArrList
	// Params   : int num - the element to remove
	// Return   : boolean true - if the element was removed
	//          : boolean false - if the element was not removed
	boolean removeByValue(int num) throws EmptyListException
	{
		if(isEmpty())
			throw new EmptyListException();
		int index = indexOf(num);
		if(index == -1)
			return false;
		removeByIndex(index);
		return true;
	}
 
 
	// Method   : set
	// Purpose  : replace the element at index index with num
	// Params   : int index - the index to replace the element at
	// Return   : int retval - the element that was replaced
	int set(int index, int num)
	{
		int retval = array.getVal(index);
		array.setVal(index, num);
		return retval;
	}
 
 
	// Method   : size
	// Purpose  : returns the number of elements in the ArrList
	// Params   : none
	// Return   : int size - the number of elements in the ArrList
	int size()
	{
		return size;
	}
 
 
	// Method   : getAccessCount
	// Purpose  : returns the number of times the array was accessed
	// Params   : none
	// Return   : int array.getAccessCount() - the number of times the array was accessed
	int getAccessCount()
	{
		return array.getAccessCount();
	}
 
 
	// Method   : resetAccessCount
	// Purpose  : resets the number of times the array was accessed to 0
	// Params   : none
	// Return   : void
	void resetAccessCount()
	{
		this.array.resetAccessCount();
	}
 
 
	// Method   : printList
	// Purpose  : prints the elements in the ArrList
	// Params   : none
	// Return   : void
	void printList()
	{
		int i = first;
		while(i != last)
		{
			System.out.print(array.getVal(i) + " ");
			i = increment(i);
		}
	}
 
 
	// Method   : shiftLeft
	// Purpose  : shifts all the elements from index i to the start of the list
	// Params   : int i - the index to start shifting elements from
	//          : int by - the number of positions to shift the elements by
	// Return   : void
	private void shiftLeft(int i, int by)
	{
		int j = decrement(i, by);
		while(i != last) {
			array.setVal(j, array.getVal(i));
			i = increment(i);
			j = increment(j);
		}
	}
 
 
	// Method   : shiftRight
	// Purpose  : shifts all the elements from index i to the end of the list
	// Params   : int i - the index to start shifting elements from
	// Return   : void
	private void shiftRight(int i)
	{
		int j = last;
		int k = decrement(j, 1);
		while(j != i)
			{
				array.setVal(j, array.getVal(k));
				j = decrement(j, 1);
				k = decrement(k, 1);
			}
	}
 
 
	// Method   : increment
	// Purpose  : increments <i> by 1, wrapping it around when necessary
	// Params   : int i - the index to increment
	// Return   : int (i+1)%array.length() - the incremented index
	private int increment(int i)
	{
		return (i + 1) % array.length();
	}
 
 
	// Method   : decrement
	// Purpose  : decrements <i> by 1, wrapping it around when necessary
	// Params   : int i - the index to decrement
	// Return   : int (i-1)%array.length() - the decremented index
	private int decrement(int i, int by)
	{
		i = (i - by) % array.length();
		if(i < 0)
			i += array.length();
		return i;
	}
 
 
	// Method   : resize
	// Purpose  : resizes the array to the new capacity
	// Params   : int newCap - the new capacity of the array
	// Return   : void
	private void resize(int newCap)
	{
		array.resize(newCap, first, size);
	}  
 }
 