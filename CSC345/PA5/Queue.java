public class Queue<Item> {
    private Item[] queue;
    private int front;
    private int back;
    private int size;

    //constructor: create an empty Queue with initial
    //capacity of 10

    public Queue() {
	queue = (Item[]) new Object[10];
	front = 0;
        back = 0;
	size = 0;
    }

    //constructor: create an empty Queue with initial
    //capacity of n
    public Queue(int n) {
	queue = (Item[]) (new Object[n]);
	front = 0;
	back = 0;
	size = 0;
    }
    
    //add an item to the back of the Queue
    //double the array capacity if the Queue is full
    public void enqueue(Item item) {//System.out.println("enqueueing: " + item);
	queue[back] = item;
	back++;
	if(back == queue.length)
	    back = 0;
	size++;
	if(size == queue.length)
	    resize(2*queue.length);
    }

    //remove and return the front item from the Queue
    //throw EmptyQueueException if the Queue is empty
    //reduce the array capacity by half if the size 
    //of the Queue falls below 1/4 full
    public Item dequeue() {
	if(size == 0)
	    return null;
	Item ret = queue[front];
	queue[front] = null;
	front++;
	if(front == queue.length)
	    front = 0;
	size--;
	if(size < queue.length/4 && size >= 10)
	    resize(queue.length/2);
	return ret;
    }

    //return true if the Queue is empty
    //return false if the Queue is not empty
    public boolean isEmpty() {
	return size == 0;
    }

    //return the size of the Queue (i.e. the 
    //number of elements currently in the Queue)
    public int size() {
	return size;
    }

    //return but do not remove the front item in the Queue
    //throw an exception if the Queue is empty
    public Item peek() {
	if(size == 0)
	    return null;
	return queue[front];
    } 
    
    //return the underlying array
    public Item[] getArray() {
	return queue;
    }

    //resize the array
    private void resize(int max) {
	Item[] newArray = (Item[]) new Object[max];
	int j = front;
	for(int i = 0; i < size; i++) {
	    if(j == queue.length)
		j = 0;
	    newArray[i] = queue[j];
	    j++;
	}
	queue = newArray;
	front = 0;
	back = size;
    }
}