/**
 * @author Brewer, Ben
 * @File  count.c
 * @Date  9/22/23
 * @Desc  counts and displays amount of occurrences and prints them in ascending order
 */
#include <stdio.h>
#include <stdlib.h>

// hashtable
typedef struct {
    int value;
    int count;
} IntCount;

int compareIntCounts(const void *a, const void *b) {
    return ((IntCount *)a)->value - ((IntCount *)b)->value;
}

int main() {
    int count;
    int retval = scanf("%d", &count);
    if (retval <=0 || count <= 0) {
        fprintf(stderr, "Invalid input.\n");
        return 1;
    }
    IntCount *counts = (IntCount *)malloc(count * sizeof(IntCount));
    if (counts == NULL) {
        fprintf(stderr, "Memory allocation failed.\n");
        return 1;
    }
    int numCounts = 0;

    for (int i = 0; i < count; i++) {
        int num;
        if (scanf("%d", &num) != 1) {
            fprintf(stderr, "Error reading input.\n");
            free(counts);
            return 1;
        }

        int found = 0;
        for (int j = 0; j < numCounts; j++) {
            if (counts[j].value == num) {
                counts[j].count++;
                found = 1;
                break;
            }
        }

        if (!found) {
            counts[numCounts].value = num;
            counts[numCounts].count = 1;
            numCounts++;
        }
    }

    qsort(counts, numCounts, sizeof(IntCount), compareIntCounts);

    for (int i = 0; i < numCounts; i++) {
        printf("%d %d\n", counts[i].value, counts[i].count);
    }
    
    free(counts);
    return 0;
}