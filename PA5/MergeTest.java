import java.util.Random;
import java.util.Arrays;
public class MergeTest {
    private static int[] sizes = new int[]{12, 52, 134, 256, 1500};
    private static int[] counts = new int[]{176, 1216, 4185, 8192, 63968};
    public static void main(String[] args) {
	Random gen = new Random(System.currentTimeMillis());
	int totalScore = 0;
	for(int t = 1; t <= 5; t++) {
	    int score = 0;
	    System.out.println("\n*****BEGIN TEST " + t + "*****");
	    int N = sizes[t-1];
	    int[] arr = new int[N];
	    int[] copy = new int[N];
	    for(int i = 0; i < N; i++) {
		arr[i] = gen.nextInt(1000);
		copy[i] = arr[i];
	    }
	    Arrays.sort(copy);
	    Array array = new Array(arr);
	    Merge.sort(array);
	    int count = array.getAccessCount();
	    //check that array is sorted
	    boolean passed = true;
	    if(array.length() != copy.length)
		passed = false;
	    for(int i = 0; i < array.length(); i++) {
		if(array.getVal(i) != copy[i]) {
		    System.out.println("The array is not sorted correctly.");
		    passed = false;
		    break;
		}
	    }
	    if(passed)
		score += 2;
	    //check the access count
	    if(passed && count <= 1.5*counts[t-1]) 
		score += 1;
	    else
		System.out.println("The access count is too high.");

	    System.out.println("Expected score for test " + t + ": " + score);
	    totalScore += score;
	}
	System.out.println("\nTotal expected score for Part 1: " + totalScore);
    }
}
	
