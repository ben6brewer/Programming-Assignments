/**
 * @author Ben Brewer
 * @file   shortestPaths.c
 * @date   10/27/23
 * @desc:  This program builds a graph from an input file and allows the user
 *         to get the min distance between two nodes using dijkstras algo
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <limits.h>

// Type aliases
typedef struct edge edge;
typedef struct node node;

// global variable to store data structure
node* nodeHead = NULL;

// Declarations
node* addNode(char *name);
int addEdge(node *node1, node *node2, int distance);
node* searchNode(char *str);
int searchDijkstras(char* nodeName1, char* nodeName2);

/**
 * @struct: node
 * @desc:   stores linked list of nodes
 * @params: char* name - string name of node
 *          edge *edges - pointer to linked list of edges
 *          node *next - pointer to next node in linked list
 *          int visited - boolean to determine if visited while traversing dijkstras
 *          int minDist - used in dijkstras algo
*/
struct node {
    char* name;
    edge *edges;
    node *next;
    int visited;
    int minDist;
};

/**
 * @struct: edge
 * @desc:   stores linked list of edges from a node
 * @params: node *to - node to connect edge from
 *          edge *next - edge to add pointer to node
 *          int weight - distance between two nodes
*/
struct edge {
    node* to;
    edge *next;
    int weight;
};


/**
 * @method: addNode
 * @desc:   adds a node to the linked list
 * @params: char* name - node name to add
 * @return: node* nodeToAdd - node that was added
*/
node* addNode(char *name) {
    node* nodeToAdd = malloc(sizeof(node));
    if (nodeToAdd == NULL)
    {
        fprintf(stderr, "bad memory\n");
        return NULL;
    }
    nodeToAdd->name = strdup(name);
    nodeToAdd->edges = NULL;
    nodeToAdd->next = nodeHead;
    nodeToAdd->visited = 0;
    nodeToAdd->minDist = 0;
    nodeHead = nodeToAdd;
    return nodeToAdd;
}

/**
 * @method: addEdge
 * @desc:   adds an edge to the linked list of edges from a node
 * @params: node *node1 - node to add edge from
 *          node *node2 - node to add edge to
 *          int distance - distance between the two nodes
 * @return: int 1 if there was an error adding the edge, else 0
*/
int addEdge(node *node1, node *node2, int distance) {
    if (node1 == NULL || node2 == NULL) {
        return 1;
    }
    edge *newEdge = malloc(sizeof(edge));
    if (newEdge == NULL) {
        return 1;
    }
    newEdge->to = node2;
    newEdge->next = NULL;
    newEdge->weight = distance;
    if (node1->edges == NULL) {
        node1->edges = newEdge;
    }
    else {
        edge *curr = node1->edges;
        while (curr->next) {
            if (strcmp(curr->to->name, node2->name) == 0) {
                free(newEdge);
                return 1;
            }
            curr = curr->next;
        }
        curr->next = newEdge;
    }
    return 0;
}

/**
 * @method: searchNode
 * @desc:   searches for a node based on its string name
 * @params: char *str - string to search name off of
 * @return: node* curr - node with matching string name, else NULL
*/
node* searchNode(char *str) {
    node *curr = nodeHead;
    while (curr) {
        if (strcmp(curr->name, str) == 0) {
            return curr;
        }
        curr = curr->next;
    }
    return NULL;
}

/**
 * @method: freeMemory
 * @desc:   frees up memory in the heap
 * @params: None
 * @return: None
*/
void freeMemory() {
    node *st0;
    node *st1;
    st0 = nodeHead;
    while (st0 != NULL) {
        edge *edg0, *edg1;
        st1 = st0->next;

        edg0 = st0->edges;
        while (edg0 != NULL) {
            edg1 = edg0->next;
            free(edg0);
            edg0 = edg1;
        }
        free(st0->name);
        free(st0);
        st0 = st1;
    }
    nodeHead = NULL;
}

/**
 * @method: checkValidAlphabetical
 * @desc:   checks to see if the input is valid
 * @params: char* str - string to check
 * @return: 0 if valid, else 1
*/
int checkValidAlphabetical(char* str) {
    for (int i = 0; str[i] != '\0'; i++) {
        if (!isalpha(str[i])) {
            return 1;
        }
    }
    return 0;
}

/**
 * @method: checkValidDistance
 * @desc:   checks to see if the distance is valid (0 or greater)
 * @params: int distance - distance to check
 * @return: 0 if valid, else 1
*/
int checkValidDistance(int distance) {
    if (distance >= 0) {
        return 0;
    }
    return 1;
}

/**
 * @method: buildGraph
 * @desc:   builds the graph by adding correct nodes and edges
 * @params: char *nodeName1 - name of first node to update or add
 *          char *nodeName2 - name of second node to update or add
 *          int distance - distance between the two nodes
 * @return: 0 if no errorSeen, else 1
*/
int buildGraph(char *nodeName1, char *nodeName2) {
    int errorSeen = 0;
    node *node1 = searchNode(nodeName1);
    node *node2 = searchNode(nodeName2);

    if (!node1) {
        node1 = addNode(nodeName1);
    }
    if (!node2) {
        node2 = addNode(nodeName2);
    }
    errorSeen = addEdge(node1, node2);
    errorSeen = addEdge(node2, node1);
    return errorSeen;
}

/**
 * @method: checkIfAllVisited
 * @desc:   checks to see all the nodes have been visited
 * @params: None
 * @return: 0 if false, else 1
*/
int checkIfAllVisited() {
    node *current = nodeHead;
    while (current != NULL) {
        if (!current->visited) {
            return 0;
        }
        current = current->next;
    }
    return 1;
}

/**
 * @method: getNodeLowestMinDist
 * @desc:   getter for the node with the lowest minDist
 * @params: None
 * @return: node* lowestNode - node with lowest minDist
*/
node* getNodeLowestMinDist() {
    node* current = nodeHead;
    node* lowestNode = NULL;

    while (current != NULL) {
        if (!current->visited && (lowestNode == NULL || current->minDist < lowestNode->minDist)) {
            lowestNode = current;
        }
        current = current->next;
    }
    return lowestNode;
}

/**
 * @method: searchDijkstras
 * @desc:   searches for the shortest path
 * @params: char* nodeName1 - from node
 *          char* nodeName2 - to node
 * @return: int target->minDist - min path else -1 if there was an error
*/
int searchDijkstras(char* nodeName1, char* nodeName2) {
    node *source = searchNode(nodeName1);
    node *target = searchNode(nodeName2);
    node *currNode = NULL;
    edge *currEdge = NULL;  
    int n = 0;

    if (!source || !target) {
        return -1;
    }

    node *current = nodeHead;
    while (current) {
        current->minDist = INT_MAX;
        current -> visited = 0;
        current = current->next;
    }

    source->minDist = 0;
    while (checkIfAllVisited() == 0) 
    {
        currNode = getNodeLowestMinDist();
        currNode -> visited = 1;
        currEdge = currNode ->edges;
        while (currEdge) 
        {
            node *currToNode = currEdge ->to;
            n = (currNode -> minDist + currEdge -> weight);
            if (n < currToNode -> minDist)
            {
                currToNode -> minDist = (currNode -> minDist + currEdge -> weight); 
            }
            currEdge = currEdge ->next;
        }
    }
    return target->minDist;
}

int main(int argc, char *argv[]) {
    FILE *input;
    int errorSeen = 0;

    if (argc > 1) {
        input = fopen(argv[1], "r");
    } else {
        return 1;
    }
    if (input == NULL) {
        fprintf(stderr, "bad file\n");
        return 1;
    }

    char *line = NULL;
    size_t len = 0;
    char nodeName1[65];
    char nodeName2[65];
    char badInput[65];
    int distance;

    int retval = getline(&line, &len, input);

    while (retval != -1) {
        if (retval > 0 && line[retval - 1] == '\n') {
            line[retval - 1] = '\0';
        }
        
        int items = sscanf(line, "%64s %64s %d %64s", nodeName1, nodeName2, &distance, badInput);
        if (items == 3) {
            if ((checkValidAlphabetical(nodeName1) + checkValidAlphabetical(nodeName2) + checkValidDistance(distance)) == 0) {
                errorSeen += buildGraph(nodeName1, nodeName2, distance);
            }
            else {
                fprintf(stderr, "bad commands input\n");
                errorSeen = 1;
            }
        } else 
        {
            fprintf(stderr, "illegal edge\n\n");
            errorSeen = 1;
        }
        retval = getline(&line, &len, input);
    }
    
    fclose(input);
    free(line);

    retval = scanf("%64s %64s", nodeName1, nodeName2);
    int minDistance = 0;
    while (retval > 0) {
        minDistance = searchDijkstras(nodeName1, nodeName2);
        if (minDistance == -1) {
            fprintf(stderr, "nodes do not exist\n");
            return 1;
        }
        printf("%d\n", minDistance);
        retval = scanf("%64s %64s", nodeName1, nodeName2);
    }
    if (retval == 0) {
        fprintf(stderr, "Error in input\n");
        return 1;
    }
    freeMemory();
    if (errorSeen > 0) {
        errorSeen = 1;
    }
    return errorSeen;
}