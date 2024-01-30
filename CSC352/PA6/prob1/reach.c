/**
 * @author: Ben Brewer
 * @file:   reach.c
 * @date:   10/20/23
 * @desc:   this file creates a reachability matrix using two linked lists
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Type aliases
typedef struct edge edge;
typedef struct node node;

// Declarations
int addNode(node** headNodeList, char* vName);
int addEdge(node *node1, node *node2);
node* searchNode(char *str, node *head);
int searchEdge(node *node1, node *node2);

/**
 * @struct: node
 * @desc:   stores linked list of nodes
 * @params: char* name - string name of node
 *          edge *edges - pointer to linked list of edges
 *          node *next - pointer to next node in linked list
 *          int visited - boolean to determine if visited while traversing dfs
*/
struct node {
    char* name;
    edge *edges;
    node *next;
    int visited;
};

/**
 * @struct: edge
 * @desc:   stores linked list of edges from a node
 * @params: node *to - node to connect edge from
 *          edge *next - edge to add pointer to node
*/
struct edge {
    node* to;
    edge *next;
};

/**
 * @method: setVisited
 * @desc:   resets all nodes to be not visited after traversing dfs
 * @params: node** nodeListHead - head of linked list of nodes
 * @return: None
*/
void setVisited(node** nodeListHead) {
    node *curr = *nodeListHead;
    while (curr) {
        curr -> visited = 0;
        curr = curr ->next;
    }
}

/**
 * @method: checkPath
 * @desc:   checks to see if a path exists between two nodes
 * @params: node *node1 - node to check from
 *          node *node2 - node to check to
 * @return: int 1 if path exists, else 0
*/
int checkPath(node *node1, node *node2) {
    if (strcmp(node1 -> name, node2 -> name) == 0) {
        return 1;
    }
    if (node1 -> visited == 1) {
        return 0;
    }
    node1 -> visited = 1;
    edge *curr = node1 -> edges;
    while (curr) {
        if (checkPath(curr -> to, node2) == 1) {
            return 1;
        }
        curr = curr ->next;
    }
    return 0;
}

/**
 * @method: addNode
 * @desc:   adds a node to the linked list
 * @params: node** headNodeList - head of linked list
 *          char* vName - node name to add
 * @return: int 1 if there was an error adding the node, else 0
*/
int addNode(node** headNodeList, char* vName) {
    node* nodeToAdd = malloc(sizeof(node));
    if (nodeToAdd == NULL)
    {
        return 1;
    }
    nodeToAdd->name = strdup(vName);
    nodeToAdd->edges = NULL;
    nodeToAdd->next = NULL;
    nodeToAdd->visited = 0;

    if (*headNodeList == NULL) {
        *headNodeList = nodeToAdd;
    }
    else if (searchNode(vName, *headNodeList)) {
        return 1;
    }
    else {
        node* curr = *headNodeList;
        while (curr->next) {
            curr = curr->next;
        }
        curr->next = nodeToAdd;
    }
    return 0;
}

/**
 * @method: addEdge
 * @desc:   adds an edge to the linked list of edges from a node
 * @params: node *node1 - node to add edge from
 *          node *node2 - node to add edge to
 * @return: int 1 if there was an error adding the edge, else 0
*/
int addEdge(node *node1, node *node2) {
    edge *newEdge = malloc(sizeof(edge));
    if (newEdge == NULL) {
        return 1;
    }
    newEdge->to = node2;
    newEdge->next = NULL;
    if (node1->edges == NULL) {
        node1->edges = newEdge;
    }
    else if (searchEdge(node1, node2) == 0) {
        return 1;
    }
    else {
        edge *curr = node1 -> edges;
        while (curr->next) {
            curr = curr->next;
        }
        curr->next = newEdge;
    }
    return 0;
}

/**
 * @method: searchEdge
 * @desc:   searches to see if an edge exists from one node to another
 * @params: node *node1 - node to check edge from
 *          node *node2 - node to check edge to
 * @return: int 0 if edge exists, else 1
*/
int searchEdge(node *node1, node *node2) {
    edge *currEdge = node1 -> edges;
    while (currEdge) {
        if (strcmp(currEdge-> to -> name, node2 -> name) == 0) {
            return 0;
        }
        currEdge = currEdge -> next;
    }
    return 1;
}

/**
 * @method: searchNode
 * @desc:   searches for a node based on its string name
 * @params: char *str - string to search name off of
 *          node *head - head of linked list of nodes
 * @return: node* curr - node with matching string name, else NULL
*/
node* searchNode(char *str, node *head) {
    node *curr = head;
    while (curr) {
        if (strcmp(curr -> name, str) == 0) {
            return curr;
        }
        curr = curr -> next;
    }
    return NULL;
}

/**
 * @method: freeMemory
 * @desc:   frees up memory in the heap
 * @params: node* head - head of linked list to free and traverse after program is done
 * @return: None
*/
void freeMemory(node* head) {

    node *st0;
    node *st1;
    st0 = head;
    while (st0 != NULL) {
        edge *edg0, *edg1;
        st1 = st0->next;

        edg0 = st0->edges;
        while (edg0 != NULL) {
            edg1 = edg0 -> next;
            free(edg0);
            edg0 = edg1;
        }
        free(st0->name);
        free(st0);
        st0 = st1;
    }
}
int main(int argc, char *argv[]) {
    FILE *input;
    int errorSeen = 0;

    if (argc > 1) {
        input = fopen(argv[1], "r");
    }
    else {
        input = stdin;
    }
    if (argc > 2) {
        fprintf(stderr, "too many files\n");
        errorSeen = 1;
    }
    if (input == NULL) {
        fprintf(stderr, "bad file\n");
        return 1;
    }
    
    char *line = NULL;
    size_t len = 0;
    char command [3];
    char nodeName1 [65];
    char nodeName2 [65];
    char ErrorStr [65];
    int pathExists;
    node *nodeListHead = NULL;
    node *tempNode1 = NULL;
    node *tempNode2 = NULL;
    int validInput = 0;

    int retval = getline(&line, &len, input);
    while (retval != -1) {
        if (retval > 0 && line[retval - 1] == '\n') {
            line[retval - 1] = '\0';
        }
        // ErrorStr allows items to be > 3 and therefore an error if there is extra input
        int items = sscanf(line, "%2s %64s %64s %64s", command, nodeName1, nodeName2, ErrorStr);
        if (items == 3) {
            if (strcmp(command, "@e") == 0) {
                tempNode1 = searchNode(nodeName1, nodeListHead);
                tempNode2 = searchNode(nodeName2, nodeListHead);
                if (tempNode1 != NULL && tempNode2 != NULL) {
                    errorSeen += addEdge(tempNode1, tempNode2);
                }
            }
            else if (strcmp(command, "@q") == 0) {
                tempNode1 = searchNode(nodeName1, nodeListHead);
                tempNode2 = searchNode(nodeName2, nodeListHead);
                if (tempNode1 != NULL && tempNode2 != NULL) {
                    pathExists = checkPath(tempNode1, tempNode2);
                    setVisited(&nodeListHead);
                    printf("%d\n", pathExists);
                }
                else {
                    fprintf(stderr, "bad vertex\n");
                    errorSeen += 1;
                }
            }
            else {
                fprintf(stderr, "bad input\n");
                errorSeen = 1;
            }
        }
        else if (items == 2) {
            validInput = 0;
            for (int i = 0; i < strlen(nodeName1); i++) {
                if (!isalnum(nodeName1[i])) {
                    fprintf(stderr, "bad input, non alphanumeric\n");
                    errorSeen += 1;
                    validInput = 1;
                }
            }
            if (validInput == 0) 
            {
                if (strcmp(command, "@n") == 0) {
                errorSeen += addNode(&nodeListHead, nodeName1);
            }
                else {
                    fprintf(stderr, "bad input\n");
                    errorSeen = 1;
                }
            }
        }
        else {
            fprintf(stderr, "bad input\n");
            errorSeen = 1;
        }
        retval = getline(&line, &len, input);
    }
    if (errorSeen >= 1) {
        errorSeen = 1;
    }
    free(line);
    freeMemory(nodeListHead);
    fclose(input);
    return errorSeen;
}