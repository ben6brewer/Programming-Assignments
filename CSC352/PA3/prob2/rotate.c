/**
 * @author Brewer, Ben
 * @File  rotate.c
 * @Date  9/22/23
 * @Desc  rotates a vector by a specified amount
 */

#include <stdio.h>
#include <stdlib.h>

int main() {
    int N, R;
    
    int retval = scanf("%d", &N);
    if (retval <= 0 || N <= 0) {
        fprintf(stderr, "Invalid input for N\n");
        return 1;
    }

    int *vector = (int *)malloc(N * sizeof(int));
    if (vector == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return 1;
    }
    
    for (int i = 0; i < N; i++) {
        retval = scanf("%d", &vector[i]);
        if (retval <= 0) {
            fprintf(stderr, "Error reading vector element %d\n", i + 1);
            free(vector);
            return 1;
        }
    }

    retval = scanf("%d", &R);
    if (retval <= 0 ) {
        fprintf(stderr, "Error reading R\n");
        free(vector);
        return 1;
    }

    int *rotated_vector = (int *)malloc(N * sizeof(int));
    if (rotated_vector == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        free(vector);
        return 1;
    }
    
    for (int i = 0; i < N; i++) {
        int newIndex = (i + R) % N;
        if (newIndex < 0) {
            newIndex += N;
        }
        rotated_vector[newIndex] = vector[i];
    }
    
    for (int i = 0; i < N; i++) {
        printf("%d ", rotated_vector[i]);
    }
    printf("\n");
    
    free(vector);
    free(rotated_vector);
    return 0;
}