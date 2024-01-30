/**
 * @author Ben Brewer
 * @file   changeBase.c
 * @date   9/13/23
 * @desc:  This program converts between any base 2-36 to decimal
*/

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

/**
 * @method: convertToDecimal
 * @desc:   getter for decimal value
 * @params: char *numStr - number as string to be converted to decimal
 *          int base - base to convert from
 * @return: unsigned long result - result number in decimal
*/
unsigned long convertToDecimal(char *numStr, int base) {
    unsigned long result = 0;
    int i;

    for (i = 0; numStr[i]; i++) {
        char c = tolower(numStr[i]);
        if (isdigit(c)) {
            int digit = c - '0';
            if (digit >= base) {
                fprintf(stderr, "Error: Invalid digit '%c' for base %d\n", numStr[i], base);
                exit(1);
            }
            result = result * base + digit;
        } else if (isalpha(c)) {
            int digit = c - 'a' + 10;
            if (digit >= base) {
                fprintf(stderr, "Error: Invalid digit '%c' for base %d\n", numStr[i], base);
                exit(1);
            }
            result = result * base + digit;
        } else {
            fprintf(stderr, "Error: Non-alphanumeric character '%c' in input\n", numStr[i]);
            exit(1);
        }
    }

    return result;
}

int main() {
    int base;
    if (scanf("%d", &base) != 1 || base < 2 || base > 36) {
        fprintf(stderr, "Bad value for base.\n");
        return 1;
    }

    char numStr[7]; // Maximum 6 characters for base-36 numbers + '\0'
    while (scanf("%6s", numStr) == 1) {
        unsigned long decimalValue = convertToDecimal(numStr, base);
        printf("%lu\n", decimalValue);
    }
    return 0;
}