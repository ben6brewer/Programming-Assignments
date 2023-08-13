import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;

@SuppressWarnings("unchecked")

public class HashtableTest {
    private static Hashtable table;
    private static HashMap map;
    private static ArrayList<String> nameList;
    private static ArrayList<String> inTable;
    private static Random gen;
    private static Pair[] arr1 = new Pair[]{null, new Pair("Adams", "Anna Adams"), new Pair("Smith", "Mary Smith"), null, null};
    private static Pair[] arr2 = new Pair[]{new Pair("Smith", "Mary Smith"), null, null, null, null, null, null, new Pair("Davidson", "Minnie Davidson"), new Pair("Bates", "Emma Bates"), new Pair("Adams", "Anna Adams"), new Pair("Carson", "Elizabeth Carson")};
    private static Pair[] arr3 = new Pair[]{null, null, new Pair("Davidson", "Minnie Davidson"), null, new Pair("Carson", "Elizabeth Carson"), new Pair("Ellis", "Margaret Ellis"), null, null, new Pair("Ingles", "Sarah Ingles"), null, null, new Pair("Harris", "Bertha Harris"), null, new Pair("Adams", "Anna Adams"), new Pair("George", "Alice George"), new Pair("Frank", "Ida Frank"), new Pair("Smith", "Mary Smith"), null, null, null, new Pair("Jackson", "Annie Jackson"), null, new Pair("Bates", "Emma Bates")};
    private static Pair[] arr4 = new Pair[]{null, null, null, null, new Pair("Jackson", "Annie Jackson"), null, null, null, new Pair("Ingles", "Sarah Ingles"), null, null};
    private static int n = 25;
    
    public static void main(String[] args) {
	setup();
	double score = 0.0;
	score += test1();	
	score += test2();
	System.out.println("Total Part 1 Expected Score: " + score);
    }

    private static double test1() {
	double score = 0.0;
	System.out.println("***Begin Test 1***");
	System.out.println("Testing isEmpty...");//1.0 point
	if(table.isEmpty())
	    score += 1.0;
	else
	    System.out.println("This table should be empty, but isEmpty is returning false.");
	
	System.out.println("Putting some items into the table...");
	for(int i = 0; i < n; i++) {
	    int num = gen.nextInt(100);
	    String name = nameList.get(i);
	    table.put(name, num);
	    map.put(name, num);
		// System.out.println(table);
	}

	System.out.println("Testing isEmpty...");//1.0 point
	if(!table.isEmpty())
	    score+=1.0;
	else
	    System.out.println("This table should not be empty, but isEmpty is returnning true.");
	
	System.out.println("Testing get...");//4.0 points
	int i = gen.nextInt(n);
	for(int j = 1; j <= 8; j++) {
	    String name = nameList.get(i);
	    Integer exp = (Integer)map.get(name);
	    Integer act = (Integer)table.get(name);
	    if(exp == act) {
		score += 0.5;
	    } else {
		System.out.println("The values for key " + name + " do not match.");
		System.out.println("Expected: " + exp);
		System.out.println("Actual: " + act);
	    }
	    i = (i + 2) % n;
	}

	System.out.println("Testing update...");//4.0 points
	ArrayList<String> temp = new ArrayList<String>();
	i = gen.nextInt(n);
	for(int j = 1; j <= 8; j++) {
	    String name = nameList.get(i);
	    temp.add(name);
	    i = (i + 2) % n;
	}
	for(int j = 0; j < temp.size(); j++) {
	    String name = temp.get(j);
	    int num = 101+j;
	    map.put(name, num);
	    table.put(name, num);
	}
	for(int j = 0; j < temp.size(); j++) {
	    String name = temp.get(j);
	    Integer exp = (Integer)map.get(name);
	    Integer act = (Integer)table.get(name);
	    if(exp == act) {
		score += 0.5;
	    } else {
		System.out.println("The values for key " + name + " do not match.");
		System.out.println("Expected: " + exp);
		System.out.println("Actual: " + act);
	    }
	}

	System.out.println("Testing delete...");//4.0 points
	temp = new ArrayList<String>();
	i = gen.nextInt(n);
	for(int j = 1; j <= 8; j++) {
	    String name = nameList.get(i);
	    temp.add(name);
	    i = (i + 2) % n;
	}
	for(int j = 0; j < temp.size(); j++) {
	    String name = temp.get(j);
	    map.remove(name);
	    table.delete(name);
	}
	for(int j = 0; j < temp.size(); j++) {
	    String name = temp.get(j);
	    Integer act = (Integer)table.get(name);
	    if(act == null) {
		score += 0.5;
	    } else {
		System.out.println("Key " + name + " was not deleted.");
	    }
	}

	System.out.println("Testing get for elements not in the table...");//1.0 point
	String name = nameList.get(nameList.size()-1);
	Integer act = (Integer)table.get(name);
	if(act == null)
	    score += 1.0;
	else 
	    System.out.println("Key " + name + " should not be in the table.");

	System.out.println("Testing size...");//1.0 point
	if(table.size() == map.size())
	    score += 1.0;
	else {
	    System.out.println("The table sizes do not match.");
	    System.out.println("Expected: " + map.size());
	    System.out.println("Actual: " + table.size());
	}
	System.out.println("Test 1 Score Expected Score: " + score + "\n");
	return score;
    }

    private static double test2() {
	System.out.println("***Begin Test 2***");
	double score = 0.0;
	int index = 0;
	table = new Hashtable<String, String>(5);
	//add 2 elements
	while(index < 2) {
	    String name = nameList.get(index);
	    String[] split = name.split(" ");
	    table.put(split[1], name);
	    index++;
	}
	Pair[] array = table.getTable();
	if(array == null)
	    return 0.0;
	int arrNum = 1;
	score += checkArray(array, arrNum);

	//add 3 more elements
	while(index < 5) {
	    String name = nameList.get(index);
	    String[] split = name.split(" ");
	    table.put(split[1], name);
	    index++;
	}
	array = table.getTable();
	arrNum++;
	score += checkArray(array, arrNum);

	//add 6 more elements
	while(index < 11) {
	    String name = nameList.get(index);
	    String[] split = name.split(" ");
	    table.put(split[1], name);
	    index++;
	}
	array = table.getTable();
	arrNum++;
	score += checkArray(array, arrNum);

	//delete 9 elements
	index = 0;
	while(index < 9) {
	    String name = nameList.get(index);
	    String[] split = name.split(" ");
	    table.delete(split[1]);
	    index++;
	}
	array = table.getTable();
	arrNum++;
	score += checkArray(array, arrNum);

	System.out.println("Test 2 Expected Score: " + score + "\n");
	return score;
    }

    private static double checkArray(Pair[] act, int num) {
	Pair[] exp = null;
	if(num == 1)
	    exp = arr1;
	else if(num == 2)
	    exp = arr2;
	else if(num == 3)
	    exp = arr3;
	else
	    exp = arr4;
	
	if(act.length != exp.length) {
	    System.out.println("The array sizes do not match.");
	    System.out.println("Expected: " + exp.length);
	    System.out.println("Actual: " + act.length);
	    return 0.0;
	}
	for(int i = 0; i < act.length; i++) {
	    if(act[i] == null && exp[i] == null)
		continue;
	    if(!act[i].equals(exp[i])) {//Note that a null pointer exception here means that the actual value is null but the expected value is not.
		System.out.println("The elemeents do not match at index " + i + ".");
		System.out.println("Expected: " + exp[i]);
		System.out.println("Actual: " + act[i]);
		System.out.println("Expected Array: " + Arrays.toString(exp));
		System.out.println("Actual Array: " + Arrays.toString(act));
		return 0.0;
	    }
	}
	return 1.0;
    }
       
    private static void setup() {
	gen = new Random(System.currentTimeMillis());
	table = new Hashtable<String, Integer>();
	map = new HashMap<String, Integer>();
	nameList = new ArrayList<String>();
	inTable = new ArrayList<String>();
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader("names.txt"));
	    String line = br.readLine();
	    while(line != null) {
		String[] split = line.split(",");
		nameList.add(split[0]);
		line = br.readLine();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }
}
