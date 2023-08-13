import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;

public class Grid {
    private Loc[][] grid;
    private int[][] temp;
    private int[] copy;
    private int accessCount;

    //constructor: creates a new Grid based on 
    //an input file
    //filename: the name of the file
    //isStrings: true if the the grid contains Strings;
    //false if it contains integers
    public Grid(String filename, boolean isStrings) {
	this.accessCount = 0;
	BufferedReader reader;
	try {
	    reader = new BufferedReader(new FileReader(filename));
	    String line = reader.readLine();
	    if(line != null) {
		int n = Integer.parseInt(line);
		copy = new int[n*n];
		int i = 0;
		grid = new Loc[n][n];
		line = reader.readLine();
		int r = 0;
		while(line != null) {
		    String[] str = line.split(" ");
		    if(str.length < n)
			break;
		    for(int c = 0; c < n; c++) {
			grid[r][c] = new Loc(r, c, str[c]);
			if(!isStrings)
			    copy[i] = Integer.parseInt(str[c]);
			i++;
		    }
		    r++;
		    line = reader.readLine();
		}
		reader.close();
	    } 
	}catch (Exception e) {
		e.printStackTrace();
	}
	temp = new int[this.size()][this.size()];
    }

    //creates a String representation of the Grid
    public String toString() {
	String str = "";
	for(int r = 0; r < grid.length; r++) {
	    for(int c = 0; c < grid[0].length; c++) {
		str += grid[r][c].getVal() + " ";
	    }
	    str += "\n";
	}
	return str;
    }

    //returns the size of the Grid (i.e. n for 
    //an n X n Grid--all Grids are square!    
    public int size() {
	return grid.length;
    }

    //returns the location at (i, j) or null if (i, j)
    //is outside the grid
    public Loc getLoc(int i, int j) {
	if(checkIndex(i) && checkIndex(j)) {
	    accessCount++;
	    return grid[i][j];
	}
	return null;
    }

    //returns the String value at (i, j) or
    //null if it is outside the grid
    public String getVal(int i, int j) {
	Loc loc = getLoc(i, j);
	if(loc != null)
	    return loc.getVal();
	return null;
    }

    //returns the integer value at (i, j)
    //or -999 if the location is outside the grid
    //NOTE: This only works for integer grids!
    public int getIntVal(int i, int j) {
	String str = getVal(i, j);
	if(str != null)
	    return Integer.parseInt(str);
	return -999;
    }

    //sets the value at (i, j) to v
    //NOTE: This should be used on integer grids!
    public void setIntVal(int i, int j, int v) {
	if(checkIndex(i) && checkIndex(j)) {
	    accessCount++;
	    grid[i][j].setVal("" + v + "");
	}
    }

    //sets the String value at (i, j) to v
    public void setVal(int i, int j, String v) {
	if(checkIndex(i) && checkIndex(j)) {
	    accessCount++;
	    grid[i][j].setVal(v);
	}
    }

    //returns the access count
    public int getAccessCount() {
	return this.accessCount;
    }

    //sets the value in the temp array at position (i, j) to val
    public void setTempVal(int i, int j, int val) {
	if(checkIndex(i) && checkIndex(j)) {
	    temp[i][j] = val;
	    accessCount++;
	}
    }

    //gets the value from the temp array at position (i, j)
    public int getTempVal(int i, int j) {
	if(checkIndex(i) && checkIndex(j)) {
	    accessCount++;
	    return temp[i][j];
	}
	return -1;
    }
    
    //returns true if the grid is sorted according to SortA and
    //false otherwise
    public boolean isSortedA() {
	Arrays.sort(copy);
	for(int r = 0; r < this.size(); r++) {
	    for(int c = 0; c < this.size(); c++) {
		String gVal = this.getVal(r, c);
		int cVal = copy[r*this.size() + c];
		if(Integer.parseInt(gVal) != cVal)
		    return false;
	    }
	}
	return true;
    }

    //returns true if the grid is sorted according to SortB and
    //false otherwise
    public boolean isSortedB() {
	Arrays.sort(copy);
	int[] array = new int[this.size()*this.size()];
	int i = 0;
	for(int r = 0; r < this.size(); r++) {
	    for(int c = 0; c < this.size(); c++) {
	        Loc cur = getLoc(r, c);
		Loc right = getLoc(r, c+1);
		Loc down = getLoc(r+1, c);
		array[i] = cur.getIntVal();
		i++;
		if(right != null && cur.getIntVal() > right.getIntVal()) return false;
		if(down != null && cur.getIntVal() > down.getIntVal()) return false;
	    }
	}
	Arrays.sort(array);
	for(int j = 0; j < array.length; j++) {
	    if(array[j] != copy[j]) {
		return false;
	    }
	}
	return true;
    }
	
    //swaps the values at the two locations
    public boolean swap(int i, int j, int k, int l) {
	if(!(i == k && j == l) && checkIndex(i) && checkIndex(j) && checkIndex(k) && checkIndex(l)) {
	    String temp = getVal(i, j);
	    setVal(i, j, getVal(k, l));
	    setVal(k, l, temp);
	    return true;
	}
	return false;
    }

    //checks the index to see if it is in the grid
    private boolean checkIndex(int i) {
	return i >= 0 && i < grid.length;
    }
}
    
	
