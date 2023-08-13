import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class TicketSaleTest {
    private static int numTickets;
    private static int processGap;
    public static void main(String[] args) {
	int score = 0;
	for(int i = 1; i <= 5; i++) {
	    for(int j = 1; j <= 2; j++) {
		score += test(i, j);
	    }
	}
	System.out.println("\nExpected Score: " + score + "/40");
    }

    private static int test(int input, int test) {
	System.out.println("\nBegin Test " + test + " on input " + input + "...\n");
	//set up
	int score = 0;
	String in = "input" + input + ".txt";//input file name
	ArrayList<Request> requests = readInput(in);//list of requests
	String out = "output" + input + "-" + test + ".txt";//output file name
	ArrayList<String> sales = readOutput(out);//expected list of sales
	ArrayList<String> actual = new ArrayList<String>();//actual list of sales

	TicketSale ts = new TicketSale(numTickets);
	boolean soldOut = false;
	int i = 0;
	int sold = 0;
	
	//run simulation
	//first while loop: adds requests to the TicketSale queue
	//and periodically processes the next one
	while(!soldOut && i < requests.size()) {
	    Request r = requests.get(i);
	    ts.addRequest(r);
	    if(i % processGap == 0) {
		Request processed = null;
		try {
		    processed = ts.processNext();
		} catch (SoldOutException e) {
		    actual.add("Sold Out!");
		    soldOut = true;
		}
		if(processed != null) {
		    actual.add(processed.toString());
		    sold++;
		}
	    }
	    i++;
	}
	//if the tickets are not sold out and there are still requests
	//int he queue, continue processing
	while(!soldOut && sold < requests.size()) {
	    Request processed = null;
	    try {
		processed = ts.processNext();
	    } catch (SoldOutException e) {
		actual.add("Sold Out!");
		soldOut = true;
	    }
	    if(processed != null) {
		actual.add(processed.toString());
		sold++;
	    }
	}

	//check that the requests were processed in the expected order
	if(sales.size() != actual.size()) {
	    System.out.println("The number of sales is not correct.");
	    return 0;
	}
	score += 2;
	for(int k = 0; k < sales.size(); k++) {
	    if(!sales.get(k).equals(actual.get(k))) {
		System.out.println("The requests were not processed in the expected order.");
		return score;
	    }
	}
	score += 2;
	return score;	
    }

    private static ArrayList<Request> readInput(String fn) {
	ArrayList<Request> requests = new ArrayList<Request>();
	int count = 1;
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(fn));
	    String line = br.readLine();
	    while(line != null) {
		String[] split = line.split(", ");
		Request r = new Request(split[0], (Integer.parseInt(split[1]) == 1), count);
		requests.add(r);
		count++;
		line = br.readLine();
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return requests;
    }

    private static ArrayList<String> readOutput(String fn) {
	ArrayList<String> sales = new ArrayList<String>();
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(fn));
	    String line = br.readLine();
	    if(line != null) {
		String[] split = line.split(",");
		numTickets = Integer.parseInt(split[0]);
		processGap = Integer.parseInt(split[1]);
		line = br.readLine();
	    }
	    while(line != null) {
		sales.add(line);
		line = br.readLine();
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return sales;
    }	
}
