public class ArraySortTest {
    private static int[] accessCounts = new int[]{2000, 55988, 23258, 17877, 22752, 15868, 4023};
    private static int[] dVals = new int[] {0, 0, 0, 15, 0, 0, 0};
    private static int totalAccessCount = 0;
    
    public static void main(String[] args) {
	double score = 0.0;
	//run tests for ArraySort
	for(int testnum = 6; testnum <= 6; testnum++)
	    score += test(testnum);
	//System.out.println("\nTotal Access Count: " + totalAccessCount);
	System.out.println("\nTotal Points: " + score);
    }

    private static double test(int testNum) {
	double score = 0.0;
	System.out.println("\n***** BEGIN TEST " + testNum + "*****");
	Array array = new Array("testArray" + testNum + ".txt");
	ArraySort.sort(array, testNum, dVals[testNum-1]);
	if(array.isSorted()) {
	    System.out.println("Array is sorted!");
	    score = 2.5;
	} else {
	    System.out.println("Array is not sorted.");
	    System.out.println(array);
	    return 0.0;
	}
	int c = array.getAccessCount();
	totalAccessCount += c;
	System.out.println("Your access count: " + c);
	System.out.println("My access count: " + accessCounts[testNum-1]);
	if (c <= array.length()) {
	    System.out.println("Access count is less than or equal to the size of the array? Something isn't right!");
	    return 0.0;
	} else if (c <= accessCounts[testNum-1]) {
	    System.out.println("Your access count looks great!");
	    score += 1.0;
	} else if (c <= (int)(1.5*accessCounts[testNum-1])) {
	    System.out.println("Your access count looks good but could be better!");
	    score += 0.75;
	} else if (c <= 2*accessCounts[testNum-1]) {
	    System.out.println("Your access count is ok, but it could be better!");
	    score += 0.5;
	} else {
	    System.out.println("Your access count is too high for efficiency credit.");
	}     
	return score;
    }
}
