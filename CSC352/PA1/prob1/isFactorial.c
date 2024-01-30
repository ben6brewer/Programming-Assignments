/**
 * @Author Brewer, Ben
 * @File isFactorial.c
 * @Date 9/5/23
 * @Desc Determines if the number from user input can be calculated by taking
 *       the factorial of another number
 */

#include <stdio.h>

/**
	 * @Method isFactorial
	 * @Desc checks to see if a number can be calculated by taking the factorial of another number
	 * @Params int n - number to check to see if it is factorial
	 * @Return int factorial == n ? i : 0 - true if factorial, else false  
	 */
int isFactorial(int n) {
    int factorial = 1;
    int i = 1;
    while (factorial < n) {
        i++;
        factorial *= i;
    }
    return factorial == n ? i : 0;
}

int main() {
    int n;
    int errorSeen = 0;
    int retval = scanf("%d", &n);
    while (retval == 1) {
        if (n <= 0) {
            fprintf(stderr, "Value entered not positive\n");
            errorSeen = 1;
        }
        else 
        {
            int b = isFactorial(n);

            if (b) {
                printf("%d = %d!\n", n, b);
            } else {
                printf("%d not factorial\n", n);
            }
        }
        retval = scanf("%d", &n);
    }
    if (retval != EOF) {
            fprintf(stderr, "Non-integer value entered\n");
            errorSeen = 1;
        }

    return errorSeen;
}