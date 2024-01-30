/**
 * @author Ben Brewer
 * @file   anagrams2.c
 * @date   10/13/23
 * @desc:  This program prints out a 2D linked list of anagrams in the
 *         order first by the chronological order of the first key's linked list
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// type aliases 
typedef struct LinkedListOfKeys keyNode;
typedef struct LinkedListOfStrs strNode;

/**
 * @struct: LinkedListOfKeys
 * @desc:   stores the keys of the anagrams which is the first sorted anagram
 * @fields: char sortedKey - sorted key to compare other string anagrams to
 *          keyNode *nextKey - next key
 *          strNode *nextStr - next strNode responsible for creating the second layer
 *                          to linked list
*/
struct LinkedListOfKeys {
    char* sortedKey;
    keyNode *nextKey;
    strNode *nextStr;

};

/**
 * @struct: LinkedListOfStrs
 * @desc:   stores the strings of existing anagrams in a linked list attatched to the sortedKey
 * @fields: char sortedKey - sorted key to compare other string anagrams to
 *          strNode *next - next strNode
*/
struct LinkedListOfStrs {
    char* str;
    strNode *next;
};

/**
 * @method: getSortedWord
 * @desc:   sorts word in ascii order which acts as key to be added to linked list
 * @params: char str[] - string to sort
 * @return: char* retval - sorted string
*/
char* getSortedWord(char str[]) {
    int length = strlen(str);
    char* retval = malloc(length + 1);
    if (retval == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return NULL;
    }

    strcpy(retval, str);

    for (int i = 0; retval[i] != '\0'; i++) {
        retval[i] = tolower(retval[i]);
    }


    for (int i = 0; retval[i] != '\0'; i++) {
        for (int j = 0; retval[j] != '\0'; j++) {
            if (retval[j] > retval[j + 1] && retval[j + 1] != '\0') {
                char temp = retval[j];
                retval[j] = retval[j + 1];
                retval[j + 1] = temp;
            }
        }
    }
    return retval;
}

/**
 * @method: createKeyNode
 * @desc:   creates a new keyNode and appends it to the linked list
 * @params: char* sortedKey - sorted key to make new unique keyNode
 * @return: keyNode *nodeToAdd - keyNode to add
*/
keyNode* createKeyNode(char* sortedKey) {
    keyNode *nodeToAdd = malloc(sizeof(keyNode));
    if (nodeToAdd == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return nodeToAdd;
    }
    nodeToAdd->sortedKey = strdup(sortedKey);
    nodeToAdd->nextKey = NULL;
    nodeToAdd->nextStr = NULL;

    return nodeToAdd;
}

/**
 * @method: createStrNode
 * @desc:   creates a new strNode with the string as a field and appends it to the linked list
 * @params: char* data - str to assign to strNode to add
 * @return: strNode *nodeToAdd - strNode to add
*/
strNode* createStrNode(char* str) {
    strNode *nodeToAdd = malloc(sizeof(strNode));
    if (nodeToAdd == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return nodeToAdd;
    }
    nodeToAdd->str = strdup(str);
    nodeToAdd->next = NULL;
    return nodeToAdd;
}

/**
 * @method: addNode
 * @desc:   adds a new anagram str to linked list
 * @params: keyNode *strHead - head of Linked List that stores the strings
 *          char* str - string of key to add and be printed later
 * @return: None
*/
void addNode(keyNode **keyHead, char* str) {
    char* sortedWord = getSortedWord(str);
    keyNode *currentKey = *keyHead;
    keyNode *previousKey = NULL;
    while (currentKey != NULL && strcmp(currentKey->sortedKey, sortedWord) != 0) {
        previousKey = currentKey;
        currentKey = currentKey->nextKey;
    }
    
    keyNode *newKeyNode = createKeyNode(sortedWord);
    if (currentKey == NULL) {
        currentKey = newKeyNode;
        if (previousKey == NULL) {
            currentKey->nextKey = *keyHead;
            *keyHead = currentKey;
        }
        else {
            previousKey->nextKey = currentKey;
        }
    }
    strNode *newStrNode = createStrNode(str);
    strNode *currStr = currentKey->nextStr;
    if (currStr == NULL) {
        currentKey->nextStr = newStrNode;
    }
    else {
        while (currStr->next != NULL) {
            currStr = currStr->next;
        }
        currStr->next = newStrNode;
    }
}

/**
 * @method: printAnswer
 * @desc:   prints the anagrams in the order of the first string with that unique key
 *          followed by the linked list with the matching keys, goes to next key linked list
 *          after end of the inner linked list that contains the same keys is printed
 * @params: keyNode *keyHead - head of linked list that stores the keys and attatches keys to str linked list
 * @return: None
*/
void printAnswer(keyNode *keyHead) {
    keyNode *currKey = keyHead;
    while (currKey != NULL) {
        strNode *word = currKey->nextStr;
        while (word != NULL) {
            printf("%s ", word->str);
            word = word->next;
        }
        currKey = currKey->nextKey;
        printf("\n");
    }
}

int main() {
    int errorSeen = 0;
    int validWord = 0;
    char input[65];
    int retval = scanf("%64s", input);
    keyNode* keyHead = NULL;

    while (retval > 0) {
        validWord = 0;
        for (int i = 0; input[i] != '\0'; i++) {
            if (!isalpha(input[i])) 
            {
                fprintf(stderr, "Error in input, nonalphabetical chars\n");
                validWord = 1;
                errorSeen = 1;
            }
        }
        if (validWord == 0) {
            addNode(&keyHead, input);
        }
        retval = scanf("%64s", input);
    }
    if (retval == 0) {
        fprintf(stderr, "Error in input\n");
        return 1;
    }
    else if (retval == -1) {
        printAnswer(keyHead);
    }
    return errorSeen;
}