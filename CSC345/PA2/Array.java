import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Array {
    private int[] array;//the underlying array
    private int accessCount;//counts the number of times the array is accessed

    //constructor that reads in the items (as Strings) from a file and creates the array
    public Array (String fn) {
	this.accessCount = 0;
	int n = 0;
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(fn));
	    String line = br.readLine();
	    if(line != null) {
		n = Integer.parseInt(line);
	    }
	    line = br.readLine();
	    int i = 0;
	    array = new int[n];
	    while(line != null && i < n) {
		int num = Integer.parseInt(line);
		array[i] = num;
		line = br.readLine();
		i++;
	    }
	    br.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

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

    //returns the accessCount
    public int getAccessCount() {
	return accessCount;
    }

	//sets the value at index i to val
    public void setValue(int i, int val) {
		array[i] = val;
		}
	
	//gets the value at index i
	public int getValue(int i) {
		int num = array[i];
		return num;
		}

	public int resetAccessCount()
	{
		accessCount = 0;
		return accessCount;
	}
}
