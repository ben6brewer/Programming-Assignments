/**
 * @author Brewer, Ben
 * @File  count2.c
 * @Date  9/30/23
 * @Desc  counts and displays amount of occurrences and prints them in ascending order
 *        using a linked list instead of an array
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Singly Linked List
typedef struct LinkedList node;

/**
	 * @Method LinkedList
	 * @Desc linked list that stores value, occurances in count, 
     *       and a pointer to the next node
	 */
struct LinkedList {
    int data;
    int count;
    node *next;
};

/**
	 * @Method addNode
	 * @Desc adds a node in ascending order, increments count if node already exists
	 * @Params node **head - pointer to head node
     *         int value - value of node to add
	 * @Return - void
	 */
void addNode(node **head, int value) {
    node *nodeToAdd = malloc(sizeof(node));
    if (nodeToAdd == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return;
    }
    nodeToAdd-> data = value;
    nodeToAdd -> count = 1;
    nodeToAdd-> next = NULL;
    
    if (*head == NULL || value < (*head) -> data) {
        nodeToAdd -> next = *head;
        *head = nodeToAdd;
        return;
    }

    node *curr = *head;
    node *prev = *head;
    while (curr) 
    {
        if (curr -> data == value) {
            curr -> count++;
            return;
        }
        else if (curr -> data > value) {
            nodeToAdd -> next = curr;
            prev -> next = nodeToAdd;
            return;
        }
        prev = curr;
        curr = curr -> next;
    }
    prev -> next = nodeToAdd;
}

/**
	 * @Method printAnswer
	 * @Desc prints the nodes in ascending order with their count
	 * @Params node *head - linked list head node
	 * @Return - void
	 */
void printAnswer(node *head) {
    int count;
    int input_value;
    node *curr = head;
    while (curr != NULL) {
        input_value = curr -> data;
        count = curr -> count;
        printf("%d %d\n", input_value, count);
        curr = curr -> next;
    }
}

int main() 
{
    int n;
    node *head = NULL;
    
    int retval = scanf("%d", &n);
    while (retval > 0) 
    {
        addNode(&head, n);
        retval = scanf("%d", &n);
    }
    if (retval == 0) 
    {
        fprintf(stderr, "Invalid input.\n");
        return 1;
    }
    if (retval == -1) {
        printAnswer(head);
    }
    return 0;
}