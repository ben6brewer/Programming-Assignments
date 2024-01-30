#ifndef UTILS_H
#define UTILS_H


#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <stdio.h>
#include <sys/stat.h>


// type aliases
typedef struct node node;
typedef struct edge edge;
typedef struct command command;


// Function prototypes
int createNewDependencyNode(char *str);
int createNewTargetNode(char *str);
int addEdgeList(char *dependencyList);
int addNewEdge(char str[]);
int addNewCommand(node *target, char *str);
void stripSpace(char *str);
node *getLastTarget();
node *findNode(char *name);
int postOrderTraversal(char *targetName);
void freeMemory();
node *getFirstTarget();
int parseUserCommands(int argc,char* argv[]);
int parseFile();
int executeCommands();
void printStructure();
node* findTarget(char *name);


// global variables
extern node* head;
extern FILE *input;
extern char* fileName;
extern char* targetToPrintFrom;
extern char *line;
extern size_t sz;
extern int commandsExecuted;

// structs
struct node {
   char* name;
   node *next;
   int visited;
   int target;
   int mustBuild;
   int completed;
   int doesExist;
   edge *dependency;
   command *commandListNext;
   struct timespec time;
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
