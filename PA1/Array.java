import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Array {
    private int[] array;//the underlying array
    private int accessCount;//counts the number of times the array is accessed

    //constructor that creates an array of 0's of size <cap>
    public Array (int cap) {
	this.accessCount = 0;
	this.array = new int[cap];
    }

    //gets a String representation of the array
    public String toString() {
	return Arrays.toString(array);
    }

    //gets the length of the array
    public int length() {
	return array.length;
    }

    //gets the value at index i
    public int getVal(int i) {
	int num = array[i];
	accessCount++;
	return num;
    }

    //sets the value at index i to val
    public void setVal(int i, int val) {
	array[i] = val;
	accessCount++;
    }

    //swaps the values at indexs i and j
    public void swap(int i, int j) {
	int temp = array[i];
	array[i] = array [j];
	array[j] = temp;
	accessCount+=4;
    }

    //returns the accessCount
    public int getAccessCount() {
	return accessCount;
    }

    //resets accessCount to 0--used only for testing!!!
    public void resetAccessCount() {
	this.accessCount = 0;
    }

    //resizes the array
    /**
     *@newCap: the new array capacity
     *@start: the index of the first element of the list
     *@n: the number of elements in the list
     **/
    public void resize(int newCap, int start, int n) {
	int[] temp = new int[newCap];
	int i = start;
	int j = 0;
	while (j < n) {
	    temp[j] = this.array[i];
	    j++;
	    i = (i+1)%array.length;
	    accessCount+=2;
	}
	this.array = temp;
    }	    
}
