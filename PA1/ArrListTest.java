import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ArrListTest {
    private static Random gen = new Random(System.currentTimeMillis());
    private static ArrayList<Integer> exp = new ArrayList<Integer>();
    private static ArrList act = new ArrList();
    
    public static void main(String[] args) {
	double score = 0;
	score += testIsEmpty();
	score += testAddLast();
	score += testAddFirst();
	score += testAdd();
    score += testGet();
	score += testIndexOf();
	score += testLastIndexOf();
	score += testContains();
	score += testIsEmpty();
	score += testRemoveFirst();
	score += testRemoveLast();
	score += testRemoveByIndex();
	score += testRemoveByValue();
	score += testRemoveRange();
	score += testSet();
	score += testSize();
	System.out.println("\nExpected Score: " + Math.min(40, score));
    }

    //testing isEmpty()
    private static double testIsEmpty() {
	act.resetAccessCount();
	if(exp.isEmpty() == act.isEmpty() && act.getAccessCount() == 0)
	    return 0.5;
	System.out.println("Either your isEmpty method is not correct or it is accessing the array unnecessarily.");
	return 0;
    }
    
    //testing the addLast method
    private static double testAddLast() {
	act.resetAccessCount();
	double score = 0;
	int numToAdd = gen.nextInt(1000);
	System.out.println("\nTesting addLast method...");
	int count = 0;
	while(count < numToAdd) {
	    int num = gen.nextInt(100);
	    exp.add(num);
	    act.addLast(num);
	    count++;
	}
	if(checkCounts("addLast", 3*numToAdd + 2*exp.size())) {
	    score += 2;
	}
	if(checkLists()) {
	    score += 2;
	}

	return score;
    }

    //testing the addFirst method
    private static double testAddFirst() {
	act.resetAccessCount();
	double score = 0;
	int numToAdd = gen.nextInt(1000);
	System.out.println("\nTesting addFirst method...");
	int count = 0;
	while(count < numToAdd) {
	    int num = gen.nextInt(100);
	    exp.add(0, num);
	    act.addFirst(num);
	    count++;
	}
	if(checkCounts("addFirst", 3*numToAdd + 2*exp.size())) {
	    score += 2;
	}
	if(checkLists()) {
	    score += 2;
	}

	return score;
    }

    //testing the add method
    private static double testAdd() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting add method...");
	for(int i = 0; i < 2; i++) {
	    int j = gen.nextInt(exp.size());
	    try {
		exp.add(j, 999+i);
		act.add(j, 999+i);
		if(checkCounts("add", 3*exp.size()))
		    score += 0.5;
		if(checkLists()) 
		    score += 0.5;

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return score;
    }

    //testing the get method
    private static double testGet() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting get method...");
	int count = 0;
	boolean passed = true;
	while(count < 100) {
	    int num = gen.nextInt(exp.size());
	    int e = exp.get(num);
	    int a = act.get(num);
	    if(e != a) {
		System.out.println("Values do not match at index " + num + ".");
		passed = false;
	    }
	    count++;
	}
	if(passed && checkCounts("get", 100)) {
	    score += 1;
	}
	return score;
    }

    //testing the indexOf method
    private static double testIndexOf() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting indexOf method...");
	int count = 1;
	boolean passedAccuracy = true;
	boolean passedCounts = true;
	while(count <= 100) {
	    int num = gen.nextInt(150);
	    int e = exp.indexOf(num);
	    int a = act.indexOf(num);
	    
	    if(e != a) {
		System.out.println("The expected index is " + e + ".");
		System.out.println("The index you produced is " + a + ".");
		passedAccuracy = false;
	    }
	    int c = e+1;
	    if(e == -1)
		c = exp.size();
	    if(!checkCounts("indexOf", c))
		passedCounts = false;
	    count++;
	}
	if(passedAccuracy)
	    score += 2;
	if(passedCounts)
	    score += 2;
	return score;
    }

    //testing the lastIndexOf method
    private static double testLastIndexOf() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting lastIndexOf method...");
	int count = 1;
	boolean passedAccuracy = true;
	boolean passedCounts = true;
	while(count <= 100) {
	    int num = gen.nextInt(150);
	    int e = exp.lastIndexOf(num);
	    int a = act.lastIndexOf(num);
	    
	    if(e != a) {
		System.out.println("The expected index is " + e + ".");
		System.out.println("The index you produced is " + a + ".");
		passedAccuracy = false;
	    }
	    int c = exp.size()-e;
	    if(e == -1)
		c = exp.size();
	    if(!checkCounts("lastIndexOf", c))
		passedCounts = false;
	    count++;
	}
	if(passedAccuracy)
	    score += 2;
	if(passedCounts)
	    score += 2;
	return score;
    }

    //testing the contains method
    private static double testContains() {
	act.resetAccessCount();
	int score = 0;
	System.out.println("\nTesting contains method...");
	int count = 1;
	boolean passedAccuracy = true;
	boolean passedCounts = true;
	while(count <= 50) {
	    int num = gen.nextInt(150);
	    boolean e = exp.contains(num);
	    boolean a = act.contains(num);
	    
	    if(e != a) {
		System.out.println("Results do not match.");
		System.out.println("Expected: " + e);
		System.out.println("Actual: " + a);
		passedAccuracy = false;
	    }
	    if(!checkCounts("contains", exp.size()))
	       passedCounts = false;	
	    count++;
	}
	if(passedAccuracy) {
	    score += 1;
	}
	if(passedCounts) {
	    score += 1;
	}
	return score;
    }

    //testing the removeFirst method
    private static double testRemoveFirst() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting removeFirst method...");
	int count = 1;
	int e = -1;
	int a = -1;
	while(count <= 50) {
	    try {
		e = exp.remove(0);
		a = act.removeFirst();
	    } catch (Exception ex) {
		ex.printStackTrace();
		return 0;
	    }	
	    
	    if(e != a) {
		System.out.println("Removed items do not match.");
		System.out.println("Expected: " + e);
		System.out.println("Actual: " + a);
		return 0;
	    }
		
	    count++;
	}
	if(checkCounts("removeFirst", 50)) {
	    score += 2;
	}
	if(checkLists()) {
	    score += 2;
	}

	return score;
    }

    //testing the removeLast method
    private static double testRemoveLast() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting removeLast method...");
	int count = 1;
	int e = -1;
	int a = -1;
	while(count <= 50) {
	    try {
		e = exp.remove(exp.size()-1);
		a = act.removeLast();
	    } catch (Exception ex) {
		ex.printStackTrace();
		return 0;
	    }
	    
	    if(e != a) {
		System.out.println("Removed items do not match.");
		System.out.println("Expected: " + e);
		System.out.println("Actual: " + a);
		return 0;
	    }
	    count++;
	}
	if(checkCounts("removeLast", 50)) {
	    score += 2;
	}
	if(checkLists()) {
	    score += 2;
	}

	return score;
    }

    //testing the remove-by-index method
    private static double testRemoveByIndex() {
	act.resetAccessCount();
	int score = 0;
	System.out.println("\nTesting remove-by-index method...");
	int count = 1;
	int e = -1;
	int a = -1;
	while(count <= 50) {
	    int i = gen.nextInt(exp.size());
	    try {
		e = exp.remove(i);
		a = act.removeByIndex(i);
	    } catch (Exception ex) {
		ex.printStackTrace();
		return 0;
	    }
	    
	    if(e != a) {
		System.out.println("Removed items do not match.");
		System.out.println("Expected: " + e);
		System.out.println("Actual: " + a);
		return 0;
	    }
	    count++;
	}
	if(checkCounts("removeByIndex", 75*exp.size())) {
	    score += 1;
	}
	if(checkLists()) {
	    score += 2;
	}

	return score;
    }

    //testing the remove-by-value method
    private static double testRemoveByValue() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting remove-by-value method...");
	int count = 1;
	boolean e = false;
	boolean a = false;
	while(count <= 25) {
	    int num = gen.nextInt(150);
	    try {
		e = exp.remove(new Integer(num));
		a = act.removeByValue(num);
	    } catch (Exception ex) {
		ex.printStackTrace();
		return 0;
	    }
	    
	    if(e != a) {
		System.out.println("Removed items do not match.");
		System.out.println("Expected: " + e);
		System.out.println("Actual: " + a);
		return 0;
	    }
	    count++;
	}
	if(checkCounts("removeByValue", 75*exp.size())) {
	    score += 1;
	}
	if(checkLists()) {
	    score += 1;
	}

	return score;
    }

    //testing the removeRange method
    private static double testRemoveRange() {
	act.resetAccessCount();
	int s = exp.size();
	double score = 0;
	System.out.println("\nTesting removeRange method...");
	int i = gen.nextInt(exp.size());
	int j = gen.nextInt(exp.size());
        while(i == j) {
	    j = gen.nextInt(exp.size());
	}
	if(i > j) {
	    int temp = i;
	    i = j;
	    j = temp;
	}
	try {
	    act.removeRange(i, j);
	} catch (Exception e) {
	    e.printStackTrace();
	    return 0;
	}
	int c = 0;
	int range = j - i + 1;
	while (c < range) {
	    exp.remove(i);
	    c++;
	}
	if(checkCounts("removeRange", 2*(s + j-i)))
	    score += 2;
	if(checkLists()) {
	    score += 2;
	}

	
	return score;
    }

	

    //testing the set method
    private static double testSet() {
	act.resetAccessCount();
	double score = 0;
	System.out.println("\nTesting set method...");
	int count = 1;
	int e = -1;
	int a = -1;
	while(count <= 10) {
	    int i = gen.nextInt(exp.size());
	    int num = gen.nextInt(500);
	    e = exp.set(i, num);
	    a = act.set(i, num);
	    count++;
	}
	if(checkCounts("set", 20)) {
	    score += 0.5;
	}
	if(checkLists()) {
	    score += 0.5;
	}

	return score;
    }

    //testing the size method
    private static double testSize() {
	act.resetAccessCount();
	if(exp.size() == act.size() && act.getAccessCount() == 0)
	    return 0.5;
	return 0;
    }


    private static boolean checkLists() {
	//compare list sizes
	if(exp.size() != act.size()) {
	    System.out.println("Sizes of lists do not match.");
	    return false;
	}
	//compare the elements
	for(int i = 0; i < exp.size(); i++) {
	    if(!exp.get(i).equals(act.get(i))) 
	    {
	    	
			System.out.println("Items do not match at index " + i + ".");
			System.out.println("Expected: " + exp.get(i));
			System.out.println("Actual: " + act.get(i));
			//return false;
	    }
	}
	return true;
    }

    private static boolean checkCounts(String method, int e) {
	int a = act.getAccessCount();
	//	System.out.println("actual count: " + a);
	//System.out.println("expected count: " + e);
	if(a > e) {
	    System.out.println("The access count for " + method + " is too high.");
	    System.out.println("Expected: " + e);
	    System.out.println("Actual: " + a);
	    act.resetAccessCount();
	    return false;
	}
	act.resetAccessCount();
	return true;
    }
}

    
	
