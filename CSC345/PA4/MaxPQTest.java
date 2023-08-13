import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MaxPQTest {
    public static ArrayList<Patient> patients;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;
    
    public static void main(String[] args) {
	patients = new ArrayList<Patient>();
	getPatients("patient_file.txt");
	ArrayList<Patient> list = new ArrayList<Patient>();
	double score = 0;

	//Test 1
        testNum++;
	score += test1_2(32, 25, list);

	//Test 2
        testNum++;
	score += test1_2(7, patients.size()-10, list);

	System.out.println("\nTotal Score: " + score);
    }	

    private static double test1_2(int i, int cap, ArrayList<Patient> list) {
	System.out.println("\n*****Begin Test " + testNum + "*****");
	index = i;
	MaxPQ pq = new MaxPQ();
	double score = 0;
	//check operations on empty PQ
	System.out.println("\nPQ is empty...");
	if(pq.size() == 0) {
	    printMsg(true, "size");
	    score += 1.0;//1.0
	} else 
	    printMsg(false, "size");
	if(pq.isEmpty()) {
	    score += 1.0;//2.0
	    printMsg(true, "isEmpty");
	} else
	    printMsg(false, "isEmpty");
	Patient max = null;
	try {
	    max = pq.getMax();
	} catch(EmptyQueueException e) {
	    score += 2.0;//4.0
	    printMsg(true, "getMax");
	}
	if(max != null)
	    printMsg(false, "getMax");
	max = null;
	try {
	    max = pq.delMax();
	} catch (EmptyQueueException e) {
	    score += 2.0;//6.0
	    printMsg(true, "delMax");
	}
	if(max != null)
	    printMsg(false, "delMax");

	//insert some Patients
	System.out.println("\nAdding some patients...");
	insertPatients(list, pq, (int)(cap/2));
	if(pq.size() == list.size()) {
	    score += 1.0;//7.0
	    printMsg(true, "size");
	} else
	    printMsg(false, "size");
	if(!pq.isEmpty()) {
	    score += 1.0;//8.0
	    printMsg(true, "isEmpty");
	} else
	    printMsg(false, "isEmpty");
	max = null;
	try {
	    max = pq.getMax();
	} catch(EmptyQueueException e) {
	    System.out.println("PQ should not be empty here!");
	    e.printStackTrace();
	}
	if(max == list.get(0)) {
	    score += 2.0;//10.0
	    printMsg(true, "getMax");
	} else {
	    printMsg(false, "getMax");
	}
	max = null;
	try {
	    max = pq.delMax();
	} catch(EmptyQueueException e) {
	    System.out.println("PQ should not be empty here!");
	    e.printStackTrace();
	}
	if(max == list.remove(0) && pq.size() == list.size()) {
	    score += 2.0;//12.0
	    printMsg(true, "delMax");
	} else
	    printMsg(false, "delMax");
	
	//fill up the queue
	System.out.println("\nFilling up the PQ...");
	insertPatients(list, pq, cap-list.size());	

	if(pq.size() == list.size()) {
	    score += 1.0;//13.0
	    printMsg(true, "size");
	} else
	    printMsg(false, "size");
	
	//remove all remaining patients
	boolean pass = true;
	if(list.isEmpty())
	    pass = false;
	while(!list.isEmpty()) {
	    Patient lp = list.remove(0);
	    Patient qp = null;
	    try {
		qp = pq.delMax();
	    } catch (EmptyQueueException e) {
		System.out.println("PQ should not be empty here!");
		e.printStackTrace();
	    }
	    if(lp != qp) {
		pass = false;
		System.out.println("Max patients not equal");
		printExpAct(lp.toString(), qp.toString());
		break;
	    }
	}
	if(!pq.isEmpty())
	    pass = false;
	if(pass) {
	    score += 12.0;//25.0
	    printMsg(true, "delMax until empty");
	}
	else
	    printMsg(false, "delMax until empty");
	System.out.println("\nTotal score for Test " + testNum + ": " + score);
	return score;
    }

    private static void printMsg(boolean passed, String method) {
	if(passed) 
	    System.out.println(method + " passed");
	else
	    System.out.println(method + " failed");
    }
    
    private static void printExpAct(String exp, String act) {
	System.out.println("Expected: " + exp);
	System.out.println("Actual: " + act);
    }

    private static void insertPatients(ArrayList<Patient> list, MaxPQ pq, int num) {
	int count = 0;
	while(count < num) {
	    Patient p = patients.get(index);
	    pq.insert(p);
	    insertToList(list, p); 
	    incIndex();
	    count++;
	}
    }

    private static void insertToList(ArrayList<Patient> list, Patient p) {
	for(int i = 0; i < list.size(); i++) {
	    if(p.compareTo(list.get(i)) > 0) {
		list.add(i, p);
		return;
	    }
	}
	list.add(p);
    }

    private static void getPatients(String fn) {
	BufferedReader reader;
	try {
	    reader = new BufferedReader(new FileReader(fn));
	    String line = reader.readLine();
	    while(line != null) {
		String[] split = line.split(",");
		if(split.length >= 2) {
		    Patient p = new Patient(split[0], Integer.parseInt(split[1]), time++);
		    patients.add(p);
		}
		line = reader.readLine();
	    }
	}catch (Exception e) {
	    e.printStackTrace();
	}  
    }

    private static void incIndex() {
	 index = (index + 1) % patients.size();
    }
}

    
	
