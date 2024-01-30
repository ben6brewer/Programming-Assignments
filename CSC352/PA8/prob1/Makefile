mymake2 : mymake2.o graphs.o
	gcc -g -Wall mymake2.o graphs.o -o mymake2

mymake2.o : mymake2.c utils.h
	gcc -g -Wall -c mymake2.c

graphs.o : graphs.c utils.h
	gcc -g -Wall -c graphs.c

.PHONY: clean
clean:
	rm -f mymake2 *.o