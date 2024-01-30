/**
 * @author Ben Brewer
 * @file   mymake2.c
 * @date   11/17/23
 * @desc   This file is responsible for calling methods to build
 *         and print a dependency graph and operate how a regular
 *         makefile would.
 */

#include "utils.h"

int main(int argc, char *argv[]) {
    int errorSeen = 0;
    errorSeen = parseUserCommands(argc, argv);
    if (errorSeen > 0) {
        return 1;
    }

    errorSeen = parseFile();
    if (errorSeen > 0) {
        return 1;
    }

    free(line);
    target* doesTargetExist = findTarget(targetToPrintFrom);
    if (doesTargetExist == NULL) {
        fprintf(stderr, "No target named %s defined\n", targetToPrintFrom);
        freeMemory();
        fclose(input);
        return 1;
    }

    errorSeen += printDependencyGraph(targetToPrintFrom);
    freeMemory();
    fclose(input);

    if (errorSeen > 0) {
        errorSeen = 1;
    }

    return errorSeen;
}
