#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "PathFinder.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	char *prog = argv[0];  /* program name for errors */
	Map map;

	if (argc == 1)  {
		if ((fp = fopen("../Maps/map.txt", "r")) == NULL) {
			fprintf(stderr, "%s: can't open %s\n", prog, "../Maps/map.txt");
			exit(1);
		} else {
			map = readMap(fp);
			fclose(fp);
		}
	}
	else {
		if ((fp = fopen(*++argv, "r")) == NULL) {
			fprintf(stderr, "%s: can't open %s\n", prog, *argv);
			exit(1);
		} else {
			map = readMap(fp);
			fclose(fp);
		}
	}

	if (ferror(stdout)) {
		fprintf(stderr, "%s: error writing stdout\n", prog);
		exit(2);
	}

	printMap(&map);

	exit(0);
}
