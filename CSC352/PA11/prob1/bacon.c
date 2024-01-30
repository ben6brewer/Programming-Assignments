/**
 * @author: Ben Brewer
 * @file:   bacon.c
 * @date:   12/6/23
 * @desc:   This file will get the bacon score of an actor based on
 *          a file and character based on user input. It does this by
 *          creating a graph and performing a BFS.
 */

// imports
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

typedef struct actor actor;
typedef struct movie movie;
typedef struct edge edge;
typedef struct queue queue;
typedef struct path path;

// structs
typedef struct actor
{
    char *name;
    int visited;
    actor *nextActor;
    edge *nextMovie;
    int score;
} actor;

typedef struct movie
{
    char *name;
    movie *nextMovie;
    edge *nextActor;
} movie;

typedef struct edge
{
    movie *movieTo;
    actor *actorTo;
    edge *next;
} edge;

typedef struct queue
{
    char *name;
    queue *next;
} queue;

typedef struct path
{
    char *actorName;
    char *movieName;
    struct path *next;
} path;

// Function prototypes
movie *getLastMovie();
int isMovie(char *str);
void printMovieGraph();
void printActorGraph();
void addActor(char *currMovieName, char *actorName);
void addMovie(char *movieName);
void processMovieLine(char *line);
void processActorLine(char *line);
void buildGraph(FILE *inFile);
int doesActorExist(char *name);
actor *findActor(char *actorName);
movie *findMovie(char *movieName);
void addMovieToActorList(char *movieName, char *actorName);
void addActorToMovieList(char *movieName, char *actorName);
void addToQueue(char *name);
int BFS(char *start);
void getUserInput();
void openFile(int argc, char *argv[]);
void freeMemory();
void addToPath(char *actorName, char *movieName);
void printPath(char *start);

// Global variables
int errSeen = 0;
int lInputFlag = 0;
actor *actorHead = NULL;
movie *movieHead = NULL;
queue *queueHead = NULL;
queue *queueTail = NULL;
path *pathHead = NULL;
path *pathTail = NULL;
FILE *inFile = NULL;
char *fileName = NULL;

int main(int argc, char *argv[])
{
    openFile(argc, argv);
    buildGraph(inFile);
    fclose(inFile);
    getUserInput();
    freeMemory();
    return errSeen;
}

/**
 * @function: buildGraph
 * @desc:     this function will build a movie and actor graph
 *            with connecting edges which is traversed in the BFS.
 * @params:   FILE *inFile - input file to parse
 * @return:   void
 */
void buildGraph(FILE *inFile)
{
    char *line = NULL;
    size_t sz = 0;

    while (getline(&line, &sz, inFile) > 0)
    {
        line[strcspn(line, "\n")] = '\0';
        if (isMovie(line) == 1)
        {
            char *colonPtr = strchr(line, ':');
            colonPtr += 2;
            processMovieLine(colonPtr);
        }
        else
        {
            processActorLine(line);
        }
    }
    free(line);
}

/**
 * @function: processActorLine
 * @desc:     helper function to parse a line if it is an actor line.
 *            calls addActor to add the actor to the graph
 * @params:   char *line - line to parse
 * @return:   void
 */
void processActorLine(char *line)
{
    char *actorName = strdup(line);
    if (!actorName)
    {
        perror("Memory");
        exit(1);
    }
    movie *lastMovie = getLastMovie();
    addActor(lastMovie->name, actorName);
    free(actorName);
}

/**
 * @function: processMovieLine
 * @desc:     helper function to parse a line if it is a movie line.
 *            meaning it contains a colon. Calls addMovie to add the
 *            movie to the graph
 * @params:   char *line - line to parse
 * @return:   void
 */
void processMovieLine(char *line)
{
    char *movieName = strdup(line);
    if (!movieName)
    {
        perror("Memory");
        exit(1);
    }
    addMovie(movieName);
    free(movieName);
}

/**
 * @function: addMovie
 * @desc:     helper function used to add a movie to graph
 * @params:   char *movieName - movie's name to create
 *            movie and add to graph
 * @return:   void
 */
void addMovie(char *movieName)
{
    movie *movieToAdd = malloc(sizeof(movie));
    if (!movieToAdd)
    {
        perror("Memory");
        exit(1);
    }

    movieToAdd->name = strdup(movieName);
    movieToAdd->nextActor = NULL;
    movieToAdd->nextMovie = NULL;
    if (movieHead == NULL)
    {
        movieHead = movieToAdd;
    }
    else
    {
        movie *tempMovie = movieHead;
        while (tempMovie->nextMovie != NULL)
        {
            tempMovie = tempMovie->nextMovie;
        }
        tempMovie->nextMovie = movieToAdd;
    }
}

/**
 * @function: addactor
 * @desc:     helper function used to add an actor to graph
 * @params:   char *currMovieName - movie to add actor to
 *            char *actorName - name of actor to create and add
 * @return:   void
 */
void addActor(char *currMovieName, char *actorName)
{
    if (doesActorExist(actorName) == 0)
    {
        actor *actorToAdd = malloc(sizeof(actor));
        if (!actorToAdd)
        {
            perror("Memory");
            exit(1);
        }
        actorToAdd->name = strdup(actorName);
        actorToAdd->nextActor = NULL;
        actorToAdd->nextMovie = NULL;
        actorToAdd->score = 0;
        actorToAdd->visited = 0;

        if (actorHead == NULL)
        {
            actorHead = actorToAdd;
        }
        else
        {
            actor *tempActor = actorHead;
            while (tempActor->nextActor != NULL)
            {
                tempActor = tempActor->nextActor;
            }
            tempActor->nextActor = actorToAdd;
        }
    }
    addActorToMovieList(currMovieName, actorName);
    addMovieToActorList(currMovieName, actorName);
}

/**
 * @function: printActorGraph
 * @desc:     helper function used to help debug. Prints the actor graph structure
 * @params:   none
 * @return:   void
 */
void printActorGraph()
{
    actor *actorPtr = actorHead;
    while (actorPtr != NULL)
    {
        printf("Actor: %s\n", actorPtr->name);
        edge *moviePtr = actorPtr->nextMovie;
        while (moviePtr != NULL)
        {
            printf("\tMovie: %s\n", moviePtr->movieTo->name);
            moviePtr = moviePtr->next;
        }
        actorPtr = actorPtr->nextActor;
    }
}

/**
 * @function: printMovieGraph
 * @desc:     helper function used to help debug. Prints the movie graph structure
 * @params:   none
 * @return:   void
 */
void printMovieGraph()
{
    movie *moviePtr = movieHead;
    while (moviePtr != NULL)
    {
        printf("Movie: %s\n", moviePtr->name);
        edge *actorPtr = moviePtr->nextActor;
        while (actorPtr != NULL)
        {
            printf("\tActor: %s\n", actorPtr->actorTo->name);
            actorPtr = actorPtr->next;
        }
        moviePtr = moviePtr->nextMovie;
    }
}

/**
 * @function: isMovie
 * @desc:     helper function used determine if a line in the input file
 *            is a movie line by checking for a colon char ':'
 * @params:   char *str - string to search
 * @return:   int 1 - if ':' is present, else 0
 */
int isMovie(char *str)
{
    while (*str != '\0')
    {
        if (*str == ':')
        {
            return 1;
        }
        str++;
    }
    return 0;
}

/**
 * @function: getLastMovie
 * @desc:     helper function used to get the most recent movie added
 *            to graph
 * @params:   none
 * @return:   movie *currentMovie - most recent movie, else NULL
 */
movie *getLastMovie()
{
    movie *currentMovie = movieHead;
    if (currentMovie == NULL)
    {
        return NULL;
    }
    while (currentMovie->nextMovie != NULL)
    {
        currentMovie = currentMovie->nextMovie;
    }
    return currentMovie;
}

/**
 * @function: doesActorExist
 * @desc:     helper function used to determine if the actor is already in the graph
 * @params:   char *name - name of actor to search for
 * @return:   int 1 - if actor already exists, else 0
 */
int doesActorExist(char *name)
{
    actor *currentActor = actorHead;
    while (currentActor != NULL)
    {
        if (strcmp(currentActor->name, name) == 0)
        {
            return 1;
        }
        currentActor = currentActor->nextActor;
    }
    return 0;
}

/**
 * @function: addActorToMovieList
 * @desc:     adds actor to the movie list graph
 * @params:   char *movieName - movie to add to
 *            char *actorName - actor to add to movie
 * @return:   void
 */
void addActorToMovieList(char *movieName, char *actorName)
{
    actor *currentActor = findActor(actorName);
    movie *currentMovie = findMovie(movieName);
    edge *currentEdge = malloc(sizeof(edge));
    if (!currentEdge)
    {
        fprintf(stderr, "memory error\n");
        exit(1);
    }

    currentEdge->movieTo = currentMovie;
    currentEdge->actorTo = NULL;
    currentEdge->next = NULL;

    if (currentActor->nextMovie == NULL)
    {
        currentActor->nextMovie = currentEdge;
    }
    else
    {
        edge *movieList = currentActor->nextMovie;
        while (movieList->next != NULL)
        {
            movieList = movieList->next;
        }
        movieList->next = currentEdge;
    }
}

/**
 * @function: addAMovieToActorList
 * @desc:     adds movie to the actor list graph
 * @params:   char *movieName - movie to add to add to actor
 *            char *actorName - actor to add to
 * @return:   void
 */
void addMovieToActorList(char *movieName, char *actorName)
{
    actor *currentActor = findActor(actorName);
    movie *currentMovie = findMovie(movieName);
    edge *currentEdge = malloc(sizeof(edge));
    if (!currentEdge)
    {
        fprintf(stderr, "memory error\n");
        exit(1);
    }
    currentEdge->movieTo = NULL;
    currentEdge->actorTo = currentActor;
    currentEdge->next = NULL;

    if (currentMovie->nextActor == NULL)
    {
        currentMovie->nextActor = currentEdge;
    }
    else
    {
        edge *actorList = currentMovie->nextActor;
        while (actorList->next != NULL)
        {
            actorList = actorList->next;
        }
        actorList->next = currentEdge;
    }
}

/**
 * @function: findActor
 * @desc:     helper function to find and return an actor if it exists
 * @params:   char *actorName - actor to search for
 * @return:   actor *currentActor - actor if they exist, else NULL
 */
actor *findActor(char *actorName)
{
    actor *currentActor = actorHead;
    while (currentActor != NULL)
    {
        if (strcmp(currentActor->name, actorName) == 0)
        {
            return currentActor;
        }
        currentActor = currentActor->nextActor;
    }
    return NULL;
}

/**
 * @function: findMovie
 * @desc:     helper function to find and return a movie if it exists
 * @params:   char *movieName - movie to search for
 * @return:   movie *currentMovie - movie if they exist, else NULL
 */
movie *findMovie(char *movieName)
{
    movie *currentMovie = movieHead;
    while (currentMovie != NULL)
    {
        if (strcmp(currentMovie->name, movieName) == 0)
        {
            return currentMovie;
        }
        currentMovie = currentMovie->nextMovie;
    }
    return NULL;
}

/**
 * @function: BFS
 * @desc:     function used to traverse the graphs and get the bacon score
 * @params:   char *start - actor name to start from
 * @return:   int 0 - if starting actor is Kevin Bacon
 *            int currentactor->score - bacon score of the actor
 *            int -1 - if there is no bacon score, meaning a path does not exist
 */
int BFS(char *start)
{
    actor *startingActor = findActor(start);
    char *target = "Kevin Bacon";

    if (strcmp(target, start) == 0)
    {
        return 0;
    }
    startingActor->visited = 1;
    startingActor->score = 0;
    addToQueue(startingActor->name);
    while (queueHead != NULL)
    {
        queue *currentQueue = queueHead;
        queueHead = queueHead->next;
        actor *currentActor = findActor(currentQueue->name);
        edge *movieList = currentActor->nextMovie;
        while (movieList != NULL)
        {
            movie *currentMovie = movieList->movieTo;
            edge *actorList = currentMovie->nextActor;

            while (actorList != NULL)
            {
                actor *currActor = actorList->actorTo;
                if (strcmp(currActor->name, target) == 0)
                {
                    addToPath(currActor->name, currentMovie->name);
                    addToPath(start, currentMovie->name);

                    return currentActor->score + 1;
                }
                if (currActor->visited != 1)
                {
                    currActor->visited = 1;
                    currActor->score = currentActor->score + 1;
                    addToQueue(currActor->name);
                    addToPath(currActor->name, currentMovie->name);
                }
                actorList = actorList->next;
            }
            movieList = movieList->next;
        }
    }
    return -1;
}

/**
 * @function: addToQueue
 * @desc:     helper function perform the BFS
 * @params:   char *name - name of node to add to queue
 * @return:   void
 */
void addToQueue(char *name)
{
    queue *nodeToAdd = malloc(sizeof(queue));
    nodeToAdd->name = strdup(name);
    nodeToAdd->next = NULL;
    if (queueHead == NULL)
    {
        queueHead = nodeToAdd;
        queueTail = nodeToAdd;
    }
    else
    {
        queueTail->next = nodeToAdd;
        queueTail = nodeToAdd;
    }
}

/**
 * @function: getUserInput
 * @desc:     gets user input from stdin and calls BFS to get the bacon
 *            score for that actor given from stdin
 * @params:   none
 * @return:   void
 */
void getUserInput()
{
    char *name = NULL;
    size_t len = 0;
    int baconScore = 0;
    while (getline(&name, &len, stdin) > 0)
    {
        name[strcspn(name, "\n")] = '\0';
        if (findActor(name) == 0 && (strcmp(name, "Kevin Bacon") != 0))
        {
            fprintf(stderr, "No actor named %s entered\n", name);
            errSeen = 1;
        }
        else
        {
            baconScore = BFS(name);
            if (baconScore == -1)
            {
                printf("Score: No Bacon!\n");
            }
            else
            {
                printf("Score: %d\n", baconScore);
            }
            if (lInputFlag == 1)
            {
                printPath(name);
            }
        }
    }
    free(name);
}

/**
 * @function: openFile
 * @desc:     opens the file given from command line arguments
 * @params:   int argc - number of arguments given form command
 *            char *argv[] - array of commands
 * @return:   void
 */
void openFile(int argc, char *argv[])
{
    if (argc < 2)
    {
        fprintf(stderr, "Usage ./exBacon [-l] fileName\n");
        exit(1);
    }

    for (int i = 1; i < argc; i++)
    {
        if (argv[i][0] == '-')
        {
            if (strcmp(argv[i], "-l") == 0)
            {
                lInputFlag = 1;
            }
            else
            {
                fprintf(stderr, "Usage ./exBacon [-l] fileNames\n");
                exit(1);
            }
        }
        else if (!fileName)
        {
            fileName = argv[i];
        }
        else
        {
            fprintf(stderr, "Usage ./exBacon [-l] fileName\n");
            exit(1);
        }
    }
    inFile = fopen(fileName, "r");
    if (!inFile)
    {
        perror(fileName);
        exit(1);
    }
}

/**
 * @function: freeMemory
 * @desc:     free's up heap after program is done
 * @params:   none
 * @return:   void
 */
void freeMemory()
{
    // Free actors
    actor *currentActor = actorHead;
    while (currentActor != NULL)
    {
        actor *nextActor = currentActor->nextActor;
        free(currentActor->name);

        // Free edges
        edge *nextMovie = currentActor->nextMovie;
        while (nextMovie != NULL)
        {
            edge *temp = nextMovie;
            nextMovie = nextMovie->next;
            free(temp);
        }
        free(currentActor);
        currentActor = nextActor;
    }

    // Free movies
    movie *currentMovie = movieHead;
    while (currentMovie != NULL)
    {
        movie *nextMovie = currentMovie->nextMovie;
        free(currentMovie->name);

        // Free edges
        edge *nextActor = currentMovie->nextActor;
        while (nextActor != NULL)
        {
            edge *temp = nextActor;
            nextActor = nextActor->next;
            free(temp);
        }

        free(currentMovie);
        currentMovie = nextMovie;
    }

    // Free queue
    queue *currentQueue = queueHead;
    while (currentQueue != NULL)
    {
        queue *nextQueue = currentQueue->next;
        free(currentQueue->name);
        free(currentQueue);
        currentQueue = nextQueue;
    }

    // Free path
    path *currentPath = pathHead;
    while (currentPath != NULL)
    {
        path *nextPath = currentPath->next;
        free(currentPath->actorName);
        free(currentPath->movieName);
        free(currentPath);
        currentPath = nextPath;
    }
    free(pathHead);
    free(pathTail);
}

/**
 * @function: addToPath
 * @desc:     helper function to add actor and movie to path used to get bacon score
 * @params:   char *actorName - actor name to add to path
 *            char *movieName - movie name to add to path
 * @return:   void
 */
void addToPath(char *actorName, char *movieName)
{
    path *nodeToAdd = malloc(sizeof(path));
    if (!nodeToAdd)
    {
        fprintf(stderr, "Memory error\n");
        exit(1);
    }
    nodeToAdd->actorName = strdup(actorName);
    nodeToAdd->movieName = strdup(movieName);
    nodeToAdd->next = NULL;
    if (pathHead == NULL)
    {
        pathHead = nodeToAdd;
        pathTail = nodeToAdd;
    }
    else
    {
        pathTail->next = nodeToAdd;
        pathTail = nodeToAdd;
    }
}

/**
 * @function: printPath
 * @desc:     prints the path used to get the bacon score
 * @params:   char *start - starting actor
 * @return:   void
 */
void printPath(char *start)
{
    if (strcmp(start, "Kevin Bacon") == 0)
    {
        printf("Kevin Bacon\n");
        return;
    }
    else
    {
        path *currentPathNode = pathHead;
        path *endPathNode = pathTail;
        if (endPathNode != NULL)
        {
            printf("%s\n", endPathNode->actorName);
            printf("was in %s with\n", currentPathNode->movieName);

            while (currentPathNode != NULL)
            {
                if (strcmp(currentPathNode->actorName, "Kevin Bacon") == 0)
                {
                    printf("Kevin Bacon\n");
                    return;
                }
                else
                {
                    printf("%s\n", currentPathNode->actorName);

                    if (currentPathNode->next != NULL)
                    {
                        printf("was in %s with\n", currentPathNode->next->movieName);
                        currentPathNode = currentPathNode->next;
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
    }
}