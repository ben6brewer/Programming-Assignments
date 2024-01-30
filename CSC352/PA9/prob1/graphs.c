/**
 * @author Ben Brewer
 * @file   graphs.c
 * @date   11/10/23
 * @desc   This file contains all function definitions
 *         and code needed to run the function calls
 *         in mymake.c to execute like a makefile would
 */

#include "utils.h"

// global variables
node* head = NULL;
FILE *input = NULL;
char* targetToPrintFrom = NULL;
char *line = NULL;
size_t sz = 0;
int commandsExecuted = 0;
char* fileName = NULL;

/**
 * @method: createNewTargetNode
 * @desc:   responsible for adding a new target node to the linked list
 * @params: char* str - name of target node to add
 * @return: exit(1) if there was an error, else 0
 */
int createNewTargetNode(char *str) {

    node* targetToAdd = malloc(sizeof(node));
    if (targetToAdd == NULL) {
        fprintf(stderr, "bad memory\n");
        freeMemory();
        exit(1);
    }

    node* doesTargetExist = findNode(str);
    if (doesTargetExist != NULL) {
        doesTargetExist -> target = 1;
        free(targetToAdd);
        targetToAdd = NULL;
    }
    else {
        targetToAdd->name = strdup(str);
        targetToAdd->next = NULL;
        targetToAdd->visited = 0;
        targetToAdd->dependency = NULL;
        targetToAdd->commandListNext = NULL;
        targetToAdd ->mustBuild = 0;
        targetToAdd -> completed = 0;
        targetToAdd -> target = 1;
        targetToAdd -> time = (struct timespec){};
        targetToAdd -> doesExist = 0;

        if (head == NULL) {
            head = targetToAdd;
        } else {
            node* current = head;
            while (current->next != NULL) {
                current = current->next;
            }
            current->next = targetToAdd;
        }
    }
    return 0;
}

/**
 * @method: createNewDependencyNode
 * @desc:   responsible for adding a new dependency node to the linked list
 * @params: char* str - name of dependency node to add
 * @return: exit(1) if there was an error, else 0
 */
int createNewDependencyNode(char *str) {

    node* targetToAdd = malloc(sizeof(node));
    if (targetToAdd == NULL) {
        fprintf(stderr, "bad memory\n");
        freeMemory();
        exit(1);
    }

    node* doesTargetExist = findNode(str);
    if (doesTargetExist == NULL) {
        targetToAdd->name = strdup(str);
        targetToAdd->next = NULL;
        targetToAdd->visited = 0;
        targetToAdd->dependency = NULL;
        targetToAdd->commandListNext = NULL;
        targetToAdd ->mustBuild = 0;
        targetToAdd -> completed = 0;
        targetToAdd -> target = 0;
        targetToAdd -> time = (struct timespec){};
        targetToAdd -> doesExist = 0;
        if (head == NULL) {
        head = targetToAdd;
        } else {
            node* current = head;
            while (current->next != NULL) {
                current = current->next;
            }
            current->next = targetToAdd;
        }
    }
    else {
        free(targetToAdd);
        targetToAdd = NULL;
    }
    addNewEdge(str);

    return 0;
}

/**
 * @method: addEdgeList
 * @desc:   responsible for parsing all dependencies in a target's list
 * @params: char *dependencyList - string list of dependencies to parse and add, separated by spaces
 * @return: exit(1) if there was an error, else 0
 */
int addEdgeList(char *dependencyList) {
    char name[65];
    int n;
    int errorSeen = 0;

    while (sscanf(dependencyList, "%64s%n", name, &n) > 0) {
        stripSpace(name);
        dependencyList += n;

        node* lastTarget = getLastTarget();
        if (strcmp(name, lastTarget -> name) != 0) {
            errorSeen += createNewDependencyNode(name);
        }

        if (errorSeen > 0) {
            freeMemory();
            exit(1);
        }
    }

    return errorSeen;
}

/**
 * @method: addNewEdge
 * @desc:   responsible for adding a new dependency to the last target's linked list
 * @params: char str[] - name of dependency node to add
 * @return: exit(1) if there was an error, else 0
 */
int addNewEdge(char str[]) {
    node* target = getLastTarget();

    if (target == NULL) {
        fprintf(stderr, "target was null\n");
        freeMemory();
        exit(1);
    }

    edge* dependencyToAdd = malloc(sizeof(edge));

    if (dependencyToAdd == NULL) {
        fprintf(stderr, "bad memory when allocating dependency\n");
        freeMemory();
        exit(1);
    }

    node* targetToPointTo = findNode(str);
    if (targetToPointTo == NULL) {
        createNewDependencyNode(str);
    }
    dependencyToAdd -> next = NULL;
    dependencyToAdd->to = findNode(str);

    edge* curr = target->dependency;

    if (curr == NULL) {
        target->dependency = dependencyToAdd;
    } else {
        while (curr->next) {
            curr = curr->next;
        }
        curr->next = dependencyToAdd;
    }
    return 0;
}

/**
 * @method: addNewCommand
 * @desc:   responsible for adding a new command to the last target's linked list
 * @params: node* target - target node to add command to
 *          char str[] - name of command node to add
 * @return: exit(1) if there was an error, else 0
 */
int addNewCommand(node* target, char *str) {
    command *commandToAdd = malloc(sizeof(command));

    if (commandToAdd == NULL) {
        fprintf(stderr, "bad memory allocation for command\n");
        freeMemory();
        exit(1);
    }

    while (isspace(*str)) {
        str++;
    }

    commandToAdd->name = strdup(str);
    commandToAdd->next = NULL;

    command *currCommand = target->commandListNext;
    if (currCommand == NULL) {
        target -> commandListNext = commandToAdd;
    } else {
        while (currCommand->next != NULL) {
            currCommand = currCommand->next;
        }
        currCommand->next = commandToAdd;
    }

    return 0;
}

/**
 * @method: stripSpace
 * @desc:   strips all leading and ending white space by adjusting pointers and null character
 * @params: char* str - str to edit
 * @return: None
 */
void stripSpace(char *str) {
    while (isspace(*str)) {
        str++;
    }

    char *end = str + strlen(str) - 1;
    while (end > str && isspace(*end)) {
        end--;
    }

    *(end + 1) = '\0';
}

/**
 * @method: getLastTarget
 * @desc:   returns the most recent target node that was added
 * @params: None
 * @return: node* lastTarget - last target node
 */
node* getLastTarget() {
    node* curr = head;
    node* lastTarget = NULL;

    while (curr != NULL) {
        if (curr->target == 1) {
            lastTarget = curr;
        }
        curr = curr->next;
    }

    return lastTarget;
}

/**
 * @method: findNode
 * @desc:   searches through linked list to see if node exists
 * @params: char *name - name of node to find
 * @return: node* current - node if it exists, else NULL
 */
node* findNode(char *name) {
    node* current = head;

    while (current != NULL) {
        if (strcmp(current->name, name) == 0) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

/**
 * @method: findTarget
 * @desc:   searches through linked list to see if target node exists
 * @params: char *name - name of target node to find
 * @return: node* current - node if it exists, else NULL
 */
node* findTarget(char *name) {
    node* current = head;

    while (current != NULL) {
        if (strcmp(current->name, name) == 0 && current->target == 1) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

/**
 * @method: postOrderTraversal
 * @desc:   performs a dfs in post order, executes commands like a makefile
 * @params: char* targetName - target name to traversal from
 * @return: exit(1) if there was an error, else 0
 */
int postOrderTraversal(char* targetName) {
    node* n = findNode(targetName);
    if (n == NULL) {
        fprintf(stderr, "node was null\n");
    }
    if (n->visited) {
        return 0;
    }
    n->visited = 1;
    struct stat nStat;
    if (stat(n->name, &nStat) != 0) {
        if (!n->target) {
            fprintf(stderr, "No rule to make target %s\n", n->name);
            freeMemory();
            exit(1);
        } else {
            n->mustBuild = 1;
        }
    }
    else {
        n->doesExist = 1;
    }

    n->time.tv_sec = nStat.st_mtim.tv_sec;
    n->time.tv_nsec = nStat.st_mtim.tv_sec;
    edge* d = n->dependency;
    while (d != NULL) {
        postOrderTraversal(d->to->name);
        if (!d->to->completed) {
            fprintf(stderr, "Circular %s <- %s dependency dropped.", targetToPrintFrom, targetName);
            // exit(1);
        }
        else if (!n->mustBuild) {
            if (n->doesExist == 0 || d->to->time.tv_sec > n->time.tv_sec) {
                n->mustBuild = 1;
            }
            else if (n->doesExist == 0  || d->to->time.tv_sec == n->time.tv_sec) {
                if (d->to->time.tv_nsec > n->time.tv_nsec) {
                    n -> mustBuild = 1;
                }
            }
        }
        d = d->next;
    }

    if (n->mustBuild) {
        command* cmd = n->commandListNext;
        while (cmd != NULL) {
            printf("%s\n", cmd->name);
            int retval = system(cmd->name);
            if (retval == 0) {
                commandsExecuted += 1;
            }
            else {
                fprintf(stderr, "Command failed: %s\n", cmd->name);
                freeMemory();
                exit(1);
            }
            cmd = cmd->next;
        }
        int retval = stat(n->name, &nStat);
        if (retval != 0) {
            fprintf(stderr, "file does not exist\n");
            freeMemory();
            exit(1);
        }
        else {
            n->time.tv_sec = nStat.st_mtim.tv_sec;
            n->time.tv_nsec = nStat.st_mtim.tv_sec;
        }
    }
    n->completed = 1;
    return 0;
}

/**
 * @method: freeMemory
 * @desc:   frees up memory in the heap
 * @params: None
 * @return: None
 */
void freeMemory() {
    node* currTarget = head;

    while (currTarget != NULL) {
        edge* currDependency = currTarget->dependency;

        while (currDependency != NULL) {
            edge* tempDependency = currDependency;
            currDependency = currDependency->next;
            free(tempDependency);
        }
        command *currCommand = currTarget->commandListNext;

        while (currCommand != NULL) {
            command *tempCommand = currCommand;
            currCommand = currCommand->next;
            free(tempCommand->name);
            free(tempCommand);
        }
        node* tempTarget = currTarget;
        currTarget = currTarget->next;
        free(tempTarget->name);
        free(tempTarget);
    }
    head = NULL;
    fclose(input);
}

/**
 * @method: getFirstTarget
 * @desc:   responsible for getting the first node which
 *          is a target starting from the head
 * @params: char* str - name of target node to add
 * @return: exit(1) if there was an error, else 0
 */
node* getFirstTarget() {
    node* current = head;

    while (current != NULL) {
        if (current->target == 1) {
            return current;
        }
        current = current->next;
    }

    return NULL;
}

/**
 * @method: parseUserCommands
 * @desc:   responsible for parsing user commands in terminal
 * @params: int argc - number of arguments
 *          char* argv[] - where the arguments are stored
 * @return: exit(1) if there was an error, else 0
 */
int parseUserCommands(int argc,char* argv[]) {
    targetToPrintFrom = NULL;
    char* defaultMakefile = "myMakefile";
    fileName = defaultMakefile;
    if (argc >=1 && argc <=4) {
        if (argc == 4) {
            if (strcmp(argv[1],"-f") == 0) {
                input = fopen(argv[2], "r");
                fileName = argv[2];
                targetToPrintFrom = (argv[3]);
            }
            else if (strcmp(argv[2],"-f") == 0) {
                input = fopen(argv[3], "r");
                fileName = argv[3];
                targetToPrintFrom = (argv[1]);
            }
            else {
                freeMemory();
                fprintf(stderr, "bad input for argc == 4\n");
                exit(1);
            }
        }
        else if (argc == 3) {
            if (strcmp(argv[1],"-f") == 0) {
                input = fopen(argv[2], "r");
                fileName = argv[3];
            }
            else {
                freeMemory();
                fprintf(stderr, "bad input for argc == 3\n");
                exit(1);
            }
        }
        else if (argc == 2) {
            if (strcmp(argv[1], "-f") != 0) {
                input = fopen(defaultMakefile, "r");
                targetToPrintFrom = argv[1];
            }
            else {
                fprintf(stderr, "Usage: ./exMymake2 [-f makefile] [target]\n");
                exit(1);
            }
        }
        else if (argc == 1) {
            input = fopen(defaultMakefile, "r");
        }
        else {
            freeMemory();
            fprintf(stderr, "some other error occured\n");
            exit(1);
        }
    }
    else {
        fprintf(stderr, "Usage: ./exMymake2 [-f makefile] [target]\n");
        exit(1);
    }

    if (input == NULL) {
        fprintf(stderr, "bad file\n");
        exit(1);
    }
    return 0;
}

/**
 * @method: parseFile
 * @desc:   parses the makefile and builds structure
 * @params: None
 * @return: exit(1) if there was an error, else 0
 */
int parseFile() {
    int errorSeen = 0;
    int retval = getline(&line, &sz, input);
    while (retval != -1) {
        line[retval - 1] = '\0';

        int isBlankLine = 1;
        for (int i = 0; line[i]; i++) {
            if (!isspace((unsigned char)line[i])) {
                isBlankLine = 0;
                break;
            }
        }
        if (isBlankLine) {
            continue;
        }

        char *dependencyPtr = strchr(line, ':');
        if (dependencyPtr) {
            *dependencyPtr = '\0';
            dependencyPtr++;
            stripSpace(line);
            errorSeen += createNewTargetNode(line);
            addEdgeList(dependencyPtr);
            if (errorSeen > 0) {
                free(line);
                freeMemory();
                exit(1);
            }
        } else if (*line == '\t') {
            node* lastTarget = getLastTarget();
            if (lastTarget == NULL) {
                fprintf(stderr, "command without a target: %s\n", line+1);
                free(line);
                freeMemory();
                exit(1);
            }
            addNewCommand(lastTarget, line);
        } else {
            fprintf(stderr, "No ':' on definition line: %s\n", line);
            free(line);
            freeMemory();
            exit(1);
        }
        retval = getline(&line, &sz, input);
    }

    if (targetToPrintFrom == NULL) {
        targetToPrintFrom = getFirstTarget() -> name;
    }
    return 0;
}

/**
 * @method: executeCommands
 * @desc:   responsible for executing the commands by calling the traversal
 * @params: None
 * @return: exit(1) if there was an error, else 0
 */
int executeCommands() {
    //printStructure();
    int errorSeen = 0;
    free(line);
    node* doesTargetExist = findTarget(targetToPrintFrom);   
    if (doesTargetExist == NULL) {
        fprintf(stderr, "No target named %s defined\n", fileName);
        freeMemory();
        exit(1);
    }
    errorSeen += postOrderTraversal(targetToPrintFrom);
    if (errorSeen > 0) {
        freeMemory();
        exit(1);
    }
    if (commandsExecuted == 0) {
        printf("%s up to date.\n", targetToPrintFrom);
    }
    freeMemory();
    return errorSeen;
}


/**
 * @method: printStructure
 * @desc:   helper function responsible for printing the 
 *          dependency graph for debugging
 * @params: None
 * @return: exit(1) if there was an error, else 0
 */
void printStructure() {
    printf("\n\n\n");
    node* currNode = head;
    while (currNode != NULL) {
        printf("%s, targetVal-%d : ", currNode -> name, currNode->target);
        edge* currDependency = currNode -> dependency;
        while (currDependency != NULL) {
            printf("%s, targetVal-%d ", currDependency->to->name, currDependency->to->target);
            currDependency =currDependency->next;
        }
        printf("\n");
        command* currCommand = currNode -> commandListNext;
        while (currCommand != NULL) {
            printf("\t%s\n", currCommand -> name);
            currCommand = currCommand -> next;
        }
        currNode = currNode -> next;
    }
}