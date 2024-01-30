/**
 * @author Ben Brewer
 * @file   graphs.c
 * @date   11/10/23
 * @desc   This file contains all method definitions
 *         and code needed to run the method calls
 *         in mymake2.c to print a dependency graph
 */

#include "utils.h"

target *head = NULL;
FILE *input = NULL;
char* targetToPrintFrom = NULL;
char *line = NULL;
size_t sz = 0;

/**
 * @method: addNewTarget
 * @desc:   responsible for adding a new target to the linked list of targets
 * @params: char str[] - name of target node to add
 * @return: int 1 if there was an error, else 0
 */
int addNewTarget(char *str) {
    target* doesTargetExist = findTarget(str);
    if (doesTargetExist != NULL) {
        fprintf(stderr, "Target, %s, declared more than once\n", doesTargetExist -> name);
        return 1;
    }

    target *targetToAdd = malloc(sizeof(target));
    if (targetToAdd == NULL) {
        fprintf(stderr, "bad memory\n");
        return 1;
    }

    targetToAdd->name = strdup(str);
    targetToAdd->next = NULL;
    targetToAdd->dependencyListNext = NULL;
    targetToAdd->commandListNext = NULL;
    targetToAdd->visited = 0;
    targetToAdd->alreadyPrinted = 0;

    if (head == NULL) {
        head = targetToAdd;
    } else {
        target *current = head;
        while (current->next != NULL) {
            current = current->next;
        }
        current->next = targetToAdd;
    }

    return 0;
}

/**
 * @method: addDependencyList
 * @desc:   responsible for parsing all dependencies in a target's list
 * @params: char *dependencyList - string list of dependencies to parse and add, separated by spaces
 * @return: int 1 if there was an error, else 0
 */
int addDependencyList(char *dependencyList) {
    char name[65];
    int n;
    int errorSeen = 0;

    while (sscanf(dependencyList, "%64s%n", name, &n) > 0) {
        stripSpace(name);
        dependencyList += n;

        target* lastTarget = getLastTarget();
        if (strcmp(name, lastTarget -> name) != 0) {
            errorSeen += addNewDependency(name);
        }

        if (errorSeen > 0) {
            return 1;
        }
    }

    return errorSeen;
}

/**
 * @method: addNewDependency
 * @desc:   responsible for adding a new dependency to the last target's linked list
 * @params: char str[] - name of dependency node to add
 * @return: int 1 if there was an error, else 0
 */
int addNewDependency(char str[]) {
    target *target = getLastTarget();

    if (target == NULL) {
        fprintf(stderr, "target was null\n");
        return 1;
    }

    dependency *dependencyToAdd = malloc(sizeof(dependency));

    if (dependencyToAdd == NULL) {
        fprintf(stderr, "bad memory when allocating dependency\n");
        return 1;
    }

    dependencyToAdd->name = strdup(str);
    dependencyToAdd->next = NULL;
    dependencyToAdd->alreadyPrinted = 0;

    dependency *curr = target->dependencyListNext;

    if (curr == NULL) {
        target->dependencyListNext = dependencyToAdd;
    } else {
        while (curr->next) {
            if (strcmp(curr->name, dependencyToAdd->name) == 0) {
                free(dependencyToAdd);
                fprintf(stderr, "dependency already in list\n");
                return 1;
            }
            curr = curr->next;
        }
        curr->next = dependencyToAdd;
    }

    return 0;
}

/**
 * @method: addNewCommand
 * @desc:   responsible for adding a new command to the last target's linked list
 * @params: target *target - target to add command to
 *          char str[] - name of command node to add
 * @return: int 1 if there was an error, else 0
 */
int addNewCommand(target *target, char *str) {
    command *commandToAdd = malloc(sizeof(command));

    if (commandToAdd == NULL) {
        fprintf(stderr, "bad memory allocation for command\n");
        return 1;
    }

    //strips any leading whitespace (for some reason my method stripSpace(str) does not work here)
    while (isspace(*str)) {
        str++;
    }

    commandToAdd->name = strdup(str);
    commandToAdd->next = NULL;

    command *currCommand = target->commandListNext;

    if (currCommand == NULL) {
        target->commandListNext = commandToAdd;
    } else {
        while (currCommand->next) {
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
 * @desc:   returns the most recent target that was added
 * @params: None
 * @return: target* curr - last target
 */
target *getLastTarget() {
    target *curr = head;

    if (curr == NULL) {
        return curr;
    }

    while (curr->next != NULL) {
        curr = curr->next;
    }

    return curr;
}

/**
 * @method: findTarget
 * @desc:   searches through linked list to see if target exists
 * @params: char *name - name of target to find
 * @return: target* current - target if it exists, else NULL
 */
target *findTarget(char *name) {
    target *current = head;

    while (current != NULL) {
        if (strcmp(current->name, name) == 0) {
            return current;
        }
        current = current->next;
    }

    return NULL;
}

/**
 * @method: handleTarget
 * @desc:   handles action needed when a target is seen in traversal
 * @params: target* targetToMark - target to edit
 * @return: None
 */
void handleNode(char *nodeToMark) {
    target *currTarget = head;
    while (currTarget != NULL) {
        if (strcmp(currTarget->name, nodeToMark) == 0) {
                    currTarget->alreadyPrinted = 1;
                }
        dependency* currDependency = currTarget -> dependencyListNext;
        while (currDependency != NULL) {
            if (strcmp(currDependency->name, nodeToMark) == 0) {
                currDependency->alreadyPrinted = 1;
            }
            currDependency = currDependency->next;
        }
        currTarget = currTarget->next;
    }
}

// /**
//  * @method: handleDependency
//  * @desc:   handles action needed when a dependency is seen in traversal
//  * @params: dependency* dependencyToMark - dependency to edit
//  * @return: None
//  */
// void handleDependency(dependency *dependencyToMark) {
//     target *currTarget = head;

//     while (currTarget != NULL) {
//         dependency *currDependency = currTarget->dependencyListNext;

//         while (currDependency != NULL) {
//             if (strcmp(currDependency->name, dependencyToMark->name) == 0) {
//                 currDependency->alreadyPrinted = 1;
//                 return;
//             }
//             currDependency = currDependency->next;
//         }

//         currTarget = currTarget->next;
//     }
// }

/**
 * @method: printDependencyGraph
 * @desc:   performs a dfs in post order, prints out a dependency graph using recursion
 * @params: char* targetName - target name to traversal from
 * @return: int 1 if there was an error, else 0
 */
int printDependencyGraph(char *targetName) {
    target *currTarget = findTarget(targetName);

    if (currTarget->visited == 1) {
        fprintf(stderr, "target %s already visited\n", targetName);
        return 1;
    }

    currTarget->visited = 1;
    dependency *currDependency = currTarget->dependencyListNext;

    while (currDependency != NULL) {
        target *depTarget = findTarget(currDependency->name);

        if (depTarget != NULL) {
            printDependencyGraph(depTarget->name);
        } else if (currDependency->alreadyPrinted == 0) {
            printf("%s\n", currDependency->name);
            handleNode(currDependency -> name);
        }
        currDependency = currDependency->next;
    }

    if (currTarget->alreadyPrinted == 0) {
        handleNode(currTarget -> name);
        printf("%s\n", currTarget->name);
    }

    command *currCommand = currTarget->commandListNext;
    while (currCommand != NULL) {
            printf("  %s\n", currCommand->name);
            currCommand = currCommand->next;
        }

    return 0;
}

/**
 * @method: freeMemory
 * @desc:   frees up memory in the heap
 * @params: None
 * @return: None
 */
void freeMemory() {
    target *currTarget = head;

    while (currTarget != NULL) {
        dependency *currDependency = currTarget->dependencyListNext;

        while (currDependency != NULL) {
            dependency *tempDependency = currDependency;
            currDependency = currDependency->next;
            free(tempDependency->name);
            free(tempDependency);
        }

        command *currCommand = currTarget->commandListNext;

        while (currCommand != NULL) {
            command *tempCommand = currCommand;
            currCommand = currCommand->next;
            free(tempCommand->name);
            free(tempCommand);
        }

        target *tempTarget = currTarget;
        currTarget = currTarget->next;
        free(tempTarget->name);
        free(tempTarget);
    }

    head = NULL;
}

/**
 * @method: getFirstTarget
 * @desc:   
 * @params: 
 * @return:
 */
node *getFirstTarget() {
    return head;
}

int parseUserCommands(int argc,char* argv[]) {
    char* defaultMakefile = "makefile1"; // CHANGE TO myMakeFile
    // correct num of arguments
    if (argc >=1 && argc <=4) {
        if (argc == 4) {
            //printf("got to argc == 4\n");
            if (strcmp(argv[1],"-f") == 0) {
                input = fopen(argv[2], "r");
                targetToPrintFrom = (argv[3]);
            }
            else if (strcmp(argv[2],"-f") == 0) {
                input = fopen(argv[3], "r");
                targetToPrintFrom = (argv[1]);
            }
            else {
                freeMemory();
                fprintf(stderr, "bad input for argc == 4\n");
                return 1;
            }
        }
        else if (argc == 3) {
            //printf("got to argc == 3\n");
            if (strcmp(argv[1],"-f") == 0) {
                //printf("file:%s\n", argv[2]);
                input = fopen(argv[2], "r");
                targetToPrintFrom = NULL; // getFirstTarget() -> name;
            }
            else {
                freeMemory();
                fprintf(stderr, "bad input for argc == 3\n");
                return 1;
            }
        }
        else if (argc == 2) {
            // printf("got to argc == 2\n");
            // printf("argv[1]:%s\n", argv[1]);
            // printf("if statement expected 1:%d\n", (strcmp(argv[1],"-f") == 1));
            if (strcmp(argv[1], "-f") != 0) {
                input = fopen(defaultMakefile, "r");
                targetToPrintFrom = argv[1];
            }
            else {
                freeMemory();
                fprintf(stderr, "bad input for argc == 2\n");
                return 1;
            }
        }
        else if (argc == 1) {
            //printf("got to argc == 1\n");
            input = fopen(defaultMakefile, "r");
            targetToPrintFrom = NULL; // getFirstTarget() -> name;
        }
        else {
            freeMemory();
            fprintf(stderr, "some other error occured\n");
            return 1;
        }
    }
    else {
        freeMemory();
        fprintf(stderr, "bad number of arguments\n");
        return 1;
    }

    if (input == NULL) {
        fprintf(stderr, "bad file\n");
        freeMemory();
        return 1;
    }
    return 0;
}

int parseFile() {
    int errorSeen = 0;
    int retval = getline(&line, &sz, input);
    while (retval != -1) {
        line[retval - 1] = '\0';

        // checks to see if the current line is completely blank, if so skip
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

        // ptr to where colon begins, before colon = target, after = dependencies
        char *dependencyPtr = strchr(line, ':');
        if (dependencyPtr) {
            *dependencyPtr = '\0';
            dependencyPtr++;
            stripSpace(line);
            errorSeen += addNewTarget(line);
            addDependencyList(dependencyPtr);
            if (errorSeen > 0) {
                free(line);
                fclose(input);
                freeMemory();
                return 1;
            }
            // parse commands
        } else if (*line == '\t') {
            target* lastTarget = getLastTarget();
            if (lastTarget == NULL) {
                fprintf(stderr, "command without a target: %s\n", line+1);
                free(line);
                fclose(input);
                freeMemory();
                return 1;
            }
            addNewCommand(lastTarget, line);
        } else {
            fprintf(stderr, "No ':' on definition line: %s\n", line);
            free(line);
            fclose(input);
            freeMemory();
            return 1;
        }
        retval = getline(&line, &sz, input);
    }

    if (targetToPrintFrom == NULL) {
        targetToPrintFrom = getFirstTarget() -> name;
    }
    return 0;
}
