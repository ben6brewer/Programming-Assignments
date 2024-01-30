/**
 * @author Ben Brewer
 * @file   mymake2.c
 * @date   11/17/23
 * @desc   This file is responsible for calling methods to build
 *         and dependency graph and execute commands like a 
 *         makefile would
 */

#include "utils.h"

int main(int argc, char *argv[]) {
    int errorSeen = 0;
    errorSeen = parseUserCommands(argc, argv);
    if (errorSeen > 0) {
        exit(1);
    }

    errorSeen = parseFile();
    if (errorSeen > 0) {
        exit(1);
    }

    errorSeen = executeCommands();
    if (errorSeen > 0) {
        exit(1);
    }
    
    return errorSeen;
}
