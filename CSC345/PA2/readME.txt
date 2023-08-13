The purpose of this Programming Assignment was to write two different java methods efficiently. The efficiency was measured by using getVal and setVal in 
the given array class. Ex: array.getVal(i) would add increment an access counter by 1.

Part1.java contains the maxSum method.
This method takes in an array and is asked to calculate the max sum of n consecutive integers. This operation was to be completed in less than or equal to 
2 * size of the array. Ex: array of size 8 must have 16 or less accesses. I wrote this method by calculating the first n sum and then finding the next sum
by subtracting the value at i and m to get the next sum. This way of calculating the maxSum keeps the access count low.

Part2.java contains the divideArray method.
This method takes in an array and is asked to sort it by its sign but not neccessarily in ascending order. Ex: Unsorted Array: [0, 0, 0, 3, 3, -2, 1, -1]
becomes, Sorted Array : [-1, -2, 0, 0, 0, 1, 3, 3]. This operation must be completed without using another data structure and must be performed in less
than or equal to 4 * size of the array. I was able to write this method using three pointer values to track the index of where negatives, positives, and
zero values should go and swapping it with the current index.

Part1Test.java && Part2Test.java
These are files I used to test my algorithms on arrays of different sizes. I tested both algorithms on arrays of size 8 - 4096 incrementing by 2^n+2
this resulted in 10 tests.

I received full credit (50/50) for completing this assignment accurately and efficiently.
