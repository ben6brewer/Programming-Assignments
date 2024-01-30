#ifndef UTILS_H
#define UTILS_H


#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <stdio.h>


// type aliases
typedef struct target target;
typedef struct dependency dependency;
typedef struct command command;


// Function prototypes
int addNewNode(char *str);
int addEdgeList(char *dependencyList);
int addNewEdge(char str[]);
int addNewCommand(node *target, char *str);
void stripSpace(char *str);
node *getLastTarget();
node *findTarget(char *name);
int postOrderTraversal(char *targetName);
void freeMemory();
node *getFirstTarget();
int parseUserCommands(int argc,char* argv[]);
int parseFile();
int executeCommands();


// global variables
extern node* head;
extern FILE *input;
extern char* targetToPrintFrom;
extern char *line;
extern size_t sz;

// structs
struct node {
   char* name;
   node *next;
   int visited;
   int target;
   int mustBuild;
   int completed;
   edge *dependency;
   command *commandListNext;
};


struct edge {
   node* to;
   edge *next;
};


struct command {
   char* name;
   command *next;
};
#endif
