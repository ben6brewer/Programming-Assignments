/**
 * @Author Brewer, Ben
 * @File sumReverse.c
 * @Date 9/5/23
 * @Desc Performs addition on the reverse of a number that is inputted through the terminal
 */

#include <stdio.h>

/**
	 * @Method reverse
	 * @Desc gets the reverse of the number n by stripping the last digit
     *       and shifting it over to the right one decimal place by multiplying by a factor of 10
	 * @Params int n - number to get reverse of
	 * @Return int reversed - reversed number
	 */
int reverse(int n) 
{
    int reversed = 0;
    while (n > 0) {
        // grabs digit in ones place
        int lastDigit = n % 10;
        // shifts digits over one place to the left and adds the digit from the ones place
        reversed = reversed * 10 + lastDigit;
        // divides the number by 10 to move it one decimal place to the right
        n /= 10;
    }
    return reversed;
}

int main() 
{
    int num;
    int errorSeen = 0;
    int retval = scanf("%d", &num);
    while (retval >= 0) 
    {
        if (retval == 0) 
        {
            fprintf(stderr, "Input was not an Integer.\n");
            return 1;
        }
        if (num <= 0) {
            fprintf(stderr, "Error: Non-positive integer detected.\n");
            errorSeen = 1;
        }
        else 
        {
            int sumRev = num + reverse(num);
            printf("%d\n", sumRev);
        }
        retval = scanf("%d", &num);
    }
    return errorSeen;
}