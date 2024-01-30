/**
 * @Author Brewer, Ben
 * @File getPrime.c
 * @Date 9/5/23
 * @Desc Gets the next prime number after a number which is read through terminal input
 */

#include <stdio.h>

/**
	 * @Method isPrime
	 * @Desc checks to see if the number is a prime number by checking to see if it is divisible by another number
	 * @Params int n - number to check to see if it is prime
	 * @Return int 0 - if the number is not prime
     *         int 1 - if the number is prime
	 */
int isPrime(int n) {
    if (n <= 1) {
        return 0;
    }
    if (n <= 3) {
        return 1;
    }
    if (n % 2 == 0 || n % 3 == 0) {
        return 0;
    }
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0) {
            return 0;
        }
    }
    return 1;
}

/**
	 * @Method getNextPrime
	 * @Desc getter for next prime number after n by checking every number after n until a prime number is hit
	 * @Params int n - number to get next prime after
	 * @Return int n - next prime number
	 */
int getNextPrime(int n) {
    while (1) {
        n++;
        if (isPrime(n)) {
            return n;
        }
    }
}

int main() {
    int n;
    int errorSeen = 0;
    int retval = scanf("%d", &n);
    if (retval != 1) {
        fprintf(stderr, "Invalid.\n");
        return 1;
    }
    if (n <= 0) {
        fprintf(stderr, "Invalid int.\n");
        return 1;
    }
    int prime = getNextPrime(n);
    printf("%d\n", prime);

    return errorSeen;
}