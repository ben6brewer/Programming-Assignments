@SuppressWarnings("unchecked")

public class Deque1<Item> {
    private Item[] array;
    private int first;
    private int last;
    private int n;
    private int count;

    /***
     *constructor: default size is 10
     **/
    public Deque1() {
	this.array = (Item[]) new Object[10];
	this.first = 0;
	this.last = 0;
	this.n = 0;
	this.count = 0;
    }

    /***
     *constructor: starting capacity is <cap>
     **/
    public Deque1(int cap) {
	this.array = (Item[]) new Object[cap];
	this.first = 0;
	this.last = 0;
	this.n = 0;
	this.count = 0;
    }

    /***
     *adds <item> to the back of the deque
     ***/
    public void enqueue(Item item) {
	if(n == array.length) {
	    resize(array.length*2);
	}
	array[last] = item;
	last = increment(last);
	n++;
	count++;
    }

    /***
     *adds <item> to the front of the deque
     ***/
    public void push(Item item) {
	if(n == array.length) {
	    resize(array.length*2);
	}
	first = decrement(first);
	array[first] = item;
	n++;
	count++;
    }

    /***
     *check if the deque is empty
     ***/
    public boolean isEmpty() {
	return n == 0;
    }

    /***
     *remove and return the first item in the deque
     ***/
    public Item dequeue() throws EmptyDequeException {
	 if(isEmpty())
	     throw new EmptyDequeException();
	 Item ret = array[first];
	 first = increment(first);
	 n--;
	 count++;
	 if(n < array.length/4 && array.length/2 >= 10)
	     resize(array.length/2);
	 return ret;
    }

     /***
     *return but do not remove the first item in the deque
     ***/
    public Item peek() throws EmptyDequeException {
	if(isEmpty())
	    throw new EmptyDequeException();
	count++;
	return array[first];
    }

    /***
     *return the size of the deque
     ***/
    public int size() {
	return n;
    }

    /***
     *return the access count 
     ***/
    public int getAccessCount() {
	return this.count;
    }

    /***
     *reset the access count to 0
     ***/
    public void resetAccessCount() {
	this.count = 0;
    }

    /***
     *increment i, wrapping around as necessary
     **/
    private int increment(int i) {
	return (i+1)%array.length;
    }

    /***
     *decrement i, wrapping around as necessary
     ***/
    private int decrement(int i) {
	i = (i - 1) % array.length;
	if(i < 0)
	    i += array.length;
	return i;
    }

    /***
     *resize the array to size <newCap>
     ***/
    private void resize(int newCap) {
	Item[] temp = (Item[]) (new Object[newCap]);
	int c = 0;
	int i = first;
	while(c < n) {
	    temp[c] = array[i];
	    count+=2;
	    i = increment(i);
	    c++;
	}
	this.first = 0;
	this.last = n;
	this.array = temp;
    }
}
