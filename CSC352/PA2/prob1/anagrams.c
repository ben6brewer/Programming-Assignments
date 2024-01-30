/**
 * @author Ben Brewer
 * @file   anagrams.c
 * @date   9/13/23
 * @desc:  This program determines if a set of words are anagrams of the first word
*/

#include <stdio.h>
#include <string.h>
#include <ctype.h>

#define MAX_STRING_LENGTH 65

/**
 * @method: areAnagrams
 * @desc:   determines if two strings are anagrams
 * @params: const char *str1 - string one to compare
 *          const char *str2 - string two to compare
 * @return: int 1 - if the strings are anagrams, else 0
 */
int areAnagrams(const char *str1, const char *str2) {
    int count1[26] = {0};
    int count2[26] = {0};

    // Calculate character frequencies for str1
    for (int i = 0; str1[i] != '\0'; i++) {
        if (isalpha(str1[i])) {
            count1[tolower(str1[i]) - 'a']++;
        }
    }

    for (int i = 0; str2[i] != '\0'; i++) {
        if (isalpha(str2[i])) {
            count2[tolower(str2[i]) - 'a']++;
        }
    }

    for (int i = 0; i < 26; i++) {
        if (count1[i] != count2[i]) {
            return 0;
        }
    }

    return 1;
}

int main() {
    int errorSeen = 0;
    char firstString[MAX_STRING_LENGTH];
    char currentString[MAX_STRING_LENGTH];

    if (scanf("%64s", firstString) != 1) {
        fprintf(stderr, "Error: No valid input.\n");
        return 1; // Return 1 to indicate an error
    }

    // Check if the first string contains only alphabetical characters
    for (int i = 0; firstString[i] != '\0'; i++) {
        if (!isalpha(firstString[i])) {
            fprintf(stderr, "Bad first string\n");
            return 1;
        }
    }

    printf("%s\n", firstString);

    while (scanf("%64s", currentString) == 1) {
        int valid = 1; // Assume the input is valid until proven otherwise

        for (int i = 0; currentString[i] != '\0'; i++) {
            if (!isalpha(currentString[i])) {
                fprintf(stderr, "Bad Input ... non-alphabetical character\n");
                errorSeen = 1;
                valid = 0; // Mark the input as invalid
                break; // Exit the loop when a non-alphabetical character is found
            }
        }

        if (valid && areAnagrams(firstString, currentString)) {
            printf("%s\n", currentString);
        }
    }

    return errorSeen;
}