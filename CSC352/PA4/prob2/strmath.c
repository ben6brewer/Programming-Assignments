/**
 * @author Brewer, Ben
 * @File strmath.c
 * @Date 9/30/23
 * @Desc does string math and outputs answer to stdout
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

/**
	 * @Method realLength
	 * @Desc gets length of real string not including the leading 0's
	 * @Params char* str - string to check
	 * @Return int retval - length of real string, else 1 (case = 0)
	 */
int realLength(char* str) {
    int retval, i;
    int length = strlen(str);
    for (i = 0; str[i] != '\0'; i++) {
        if (str[i] != '0') {
            retval = length - i;
            return retval;
        }
    }
    return 1;
}

/**
	 * @Method getMax
	 * @Desc returns the max of two ints
	 * @Params int a - num1
     *         int b - num2
	 * @Return int a - if a > b, else b
	 */
int getMax(int a, int b)
{
    if (a > b) {
        return a;
    }
    return b;
}
/**
	 * @Method compare
	 * @Desc compares two strings to one another
	 * @Params char* str1 - string one to check
     *         char* str2 - string two to check
	 * @Return int 0 - if str2 > str1, else 1
	 */
int compare(char* str1, char* str2) {
    for (int i = 0; i < strlen(str1); i ++) {
        if (str2[i] > str1[i]) {
            return 0;
        }
    }
    return 1;
}

/**
	 * @Method sub
	 * @Desc subtracts two strings using string math
	 * @Params char* str1 - string one to subtract from
     *         char* str2 - string two to subract
	 * @Return char* retval - answer string
	 */
char* sub(char* str1, char* str2) {
    //printf("str1: %s, str2: %s\n", str1, str2);
    int str1Length = strlen(str1);
    int str2Length = strlen(str2);
    int retvalSize = getMax(str1Length, str2Length) + 2;
    char* retval = malloc(retvalSize);
    char* temp;
    if (retval == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
    }
    if (realLength(str2) > realLength(str1) || (realLength(str1) == realLength(str2) && compare(str1, str2) == 0))
    {
        printf("-");
        temp = str1;
        str1 = str2;
        str2 = temp;
    }
    int str1Index, str2Index, i, j;
    str1Index = str1Length - 1;
    str2Index = str2Length - 1;
    int str1Num = 0;
    int str2Num = 0;
    int columnDiff = 0;
    int columnNum = 0;
    int borrow = 0;
    int retvalIndex = retvalSize - 2;

    for (i = str1Index, j = str2Index; i >= 0 || j >= 0 || borrow > 0; i--, j--) {
        if (i >= 0) {
            str1Num = str1[i] - '0';
        }
        else {
            str1Num = 0;
        }
        if (j >= 0) {
            str2Num = str2[j] - '0';
        }
        else {
            str2Num = 0;
        }
        columnDiff = str1Num - str2Num - borrow;
        if (columnDiff < 0) {
            columnDiff += 10;
            borrow = 1;
        }
        else {
            borrow = 0;
        }
        columnNum = columnDiff + '0';
        retval[retvalIndex] = columnNum;
        retvalIndex--;
    }
    retval[retvalSize] = '\0';
    return retval;
}

/**
	 * @Method add
	 * @Desc adds two strings using string math
	 * @Params char* str1 - string one to add from
     *         char* str2 - string two to add
	 * @Return char* retval - answer string
	 */
char* add(char* str1, char* str2) {
    int str1Length = strlen(str1);
    int str2Length = strlen(str2);
    int retvalSize = getMax(str1Length, str2Length) + 2;
    char* retval = malloc(retvalSize);
    if (retval == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
    }

    int str1Index, str2Index, i, j;
    str1Index = str1Length - 1;
    str2Index = str2Length - 1;
    int str1Num = 0;
    int str2Num = 0;
    int columnSum = 0;
    int columnNum = 0;
    int carry = 0;
    int retvalIndex = retvalSize - 2;

    for (i = str1Index, j = str2Index; i >= 0 || j >= 0 || carry > 0; i--, j--) {
        if (i >= 0) {
            str1Num = str1[i] - '0';
        }
        else {
            str1Num = 0;
        }
        if (j >= 0) {
            str2Num = str2[j] - '0';
        }
        else {
            str2Num = 0;
        }
        columnSum = str1Num + str2Num + carry;
        carry = columnSum / 10;
        columnNum = (columnSum % 10) + '0';
        retval[retvalIndex] = columnNum;
        retvalIndex--;
    }
    retval[retvalSize] = '\0';
    return retval;
}

/**
	 * @Method stripLeadingZeroes
	 * @Desc strips the leading zeroes on a string
	 * @Params char* str - string to strip
     *         int length - real length without 0's
	 * @Return char* retval - answer string
	 */
char* stripLeadingZeroes(char* str, int length) {
    int i;
    char* retval = malloc(length + 1);
    if (retval == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return NULL;
    }

    for (i = 0; i < length; i++) {
        retval[i] = str[(strlen(str) - length) + i];
    }
    retval[length] = '\0';
    return retval;
}

int main() {
    int str1Length, str2Length;
    char *strOperation, *str1, *str2, *answer;

    int block = 0;
    size_t len = 0;
    size_t retval;
    char *line = NULL;

    while (block < 3) {
        retval = getline(&line, &len, stdin);
        if (retval == -1 || (retval == 1 && line[0] == '\n'))
        {
            fprintf(stderr, "Error reading input.\n");
            return 1;
        }
        if (block == 0) {
            line[retval - 1] = '\0';
            for (int i = 0; i < retval - 1; i++) 
            {
                if (!isdigit(line[i])) 
                {
                    fprintf(stderr, "Invalid input.\n");
                    return 1;
                }
            }
            strOperation = strtok(line, "\n");
            strOperation = stripLeadingZeroes(strOperation, strlen(strOperation));
        } else if (block == 1) {
            line[retval - 1] = '\0';
            for (int i = 0; i < retval - 1; i++) 
            {
                if (!isdigit(line[i])) 
                {
                    fprintf(stderr, "Invalid input.\n");
                    return 1;
                }
            }
            str1 = malloc(retval);
            if (str1 == NULL) {
                fprintf(stderr, "memory error\n");
                return 1;
            }
            strncpy(str1, line, retval);
        } else if (block == 2) {
            for (int i = 0; i < retval - 1; i++) 
            {
                if (!isdigit(line[i])) 
                {
                    fprintf(stderr, "Invalid input.\n");
                    return 1;
                }
            }
            str2 = malloc(retval);
            if (str2 == NULL) {
                fprintf(stderr, "memory error\n");
                return 1;
            }
            strncpy(str2, line, retval);
        }
        block++;
    }

    if (strcmp(strOperation, "add") == 0 || strcmp(strOperation, "sub") == 0) {
        str1Length = realLength(str1);
        str2Length = realLength(str2);
        str1 = stripLeadingZeroes(str1, str1Length);
        str2 = stripLeadingZeroes(str2, str2Length);

        if (strcmp(strOperation, "add") == 0) {
            answer = add(str1, str2);
            if (answer[0] == '\0') {
                printf("%s\n", &answer[1]);
            } else {
                printf("%s\n", answer);
            }
        } else if (strcmp(strOperation, "sub") == 0) {
            answer = sub(str1, str2);
            answer = answer + 1;
            if (answer[0] == '0') {
                answer = answer + 1;
                printf("%s\n", answer);
            } else {
                printf("%s\n", answer);
            }
        }
    } else {
        fprintf(stderr, "incorrect operation entered\n");
        return 1;
    }
    return 0;
}