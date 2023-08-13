import java.util.*;
import java.io.*;

public class SortGridTest {
    private static int[] c1 = new int[]{2140, 11558, 58384, 281752, 1320940};
    private static int[] c2 = new int[]{862, 6582, 51752, 361016, 2717196};
    private static int count = 0;
    
    public static void main(String[] args) {
	double score = 0.0;
	//Testing SortA
	for(int testNum = 1; testNum <= 5; testNum++) 
	    score += testSortA(testNum);

	//Testing SortB
	for(int testNum = 1; testNum <= 5; testNum++)
	    score += testSortB(testNum);
	
	//System.out.println("\nTotal Access Count = " + count);
	System.out.println("\nSortGridTest Total: " + score);
    }


    private static double testSortA(int testNum) {
	double score = 0;
	System.out.println("\n***** BEGIN TEST " + testNum +  " for SortA *****");
	Grid grid = new Grid("testGrid" + testNum + ".txt", false);
	SortGrid.sortA(grid);
	int aCount = grid.getAccessCount();
	score += checkAccuracy(grid, true);
	score += checkAccessCount(grid.size(), aCount, c1[testNum-1]);
	return score;
    }

    private static double testSortB(int testNum) {
	double score = 0;
	System.out.println("\n***** BEGIN TEST " + testNum + " for SortB *****");
	Grid grid = new Grid("testGrid" + testNum + ".txt", false);
	SortGrid.sortB(grid);
	int aCount = grid.getAccessCount();
	score += checkAccuracy(grid, false);
	score += checkAccessCount(grid.size(), aCount, c2[testNum-1]);
	return score;
    }
   
    private static double checkAccessCount(int n, int c, int c1) {
	System.out.println("Checking access count...");
	System.out.println("Your Access Count: " + c);
	int lt = n*n;
	count += c;//System.out.println(c); System.out.println(c1);
	if(c < lt) {
	    System.out.println("Access count is less than the grid size? Something isn't right...");
	    return 0.0;
	} 
	if(c <= c1) {
	    System.out.println("Access count is below the first cutoff!");
	    return 1.0;
	}
	else if(c <= c1*2.0) {
	    System.out.println("Access count is below the second cutoff!");
	    return 0.75;
	}
	else if(c <= c1*3.0) {
	    System.out.println("Access count is below the third cutoff!");
	    return 0.5;
	}
	else {
	    System.out.println("Access count is too high...");
	}
	return 0.0;
    }

    private static double checkAccuracy(Grid grid, boolean isA) {
	//System.out.println(grid);
	if(isA) {
	    if(grid.isSortedA()) {
		System.out.println("The grid is sorted!");
		return 1.0;
	    }
	    System.out.println("The grid is not sorted. Use the toString method to print the grid before and after sorting.");
	    return 0.0;
	}
	if(grid.isSortedB()) {
	    System.out.println("The grid is sorted!");
	    return 1.0;
	}
	System.out.println("The grid is not sorted. Use the toString method to print the grid before and after sorting.");
	return 0.0;
    }
}
 
