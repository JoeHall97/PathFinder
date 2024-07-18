#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Main.h"

void filecopy(FILE *ifp, FILE *ofp);

int main(int argc, char *argv[]) {
	FILE *fp;
	char *prog = argv[0];  /* program name for errors */
	Map *map;

	if (argc == 1)  {
		filecopy(stdin, stdout);
	}
	else {
		if ((fp = fopen(*++argv, "r")) == NULL) {
			fprintf(stderr, "%s: can't open %s\n", prog, *argv);
			exit(1);
		} else {
			readInMap(fp, map);
			fclose(fp);
		}
	}

	if (ferror(stdout)) {
		fprintf(stderr, "%s: error writing stdout\n", prog);
		exit(2);
	}

	printMap(map);

	exit(0);
}

/* filecopy: copy file ifp to file ofp */
void filecopy(FILE *ifp, FILE *ofp) {
	int c;

	while ((c = getc(ifp)) != EOF)
		putc(c, ofp);
}

/* getMapDimensions: finds and sets the passed in map's width & height */
int getMapDimensions(FILE *fp, Map *map) {
	int mapWidth = 0;
	int mapHeight = 0;
	int c;

	// TODO: validate the map conforms to expected format

	while ((c = getc(fp)) != EOF) {
		if (c == '\n' || c == ' ')
			break;
		++mapWidth;
	}

	++mapHeight;
	// increase row size for new line characters & in case there is any white-space
	char row[mapWidth+10];

	while (fgets(row, (mapWidth+10)*sizeof(char *), fp)) {
		if (row[0] != '|' && row[0] != '+')
			break;
		++mapHeight;
	}

	map->width = mapWidth-2;
	map->height = mapHeight-2;
	rewind(fp);
	return 0;	
}

/* readInMap: reads in a map object from a file pointer */
/* RETURNS: 0 if reading in map was successful, else -1 */
int readInMap(FILE *fp, Map *map) {
    getMapDimensions(fp, map);
	// increase row size for new line characters & in case there is any white-space
	char row[map->width+10];
	int i = 0;

	map->map = (char **) malloc(map->height * sizeof(char *));

	// ignore first row, as it's just contains boundary marks
	fgets(row, sizeof(row), fp);

	while (fgets(row, (map->width+10)*sizeof(row), fp) && i<map->height) {
		map->map[i] = (char *) malloc(map->width * sizeof(char));

		for (int j=0; j<map->width; j++) {
			map->map[i][j] = row[j+1];
		}

		++i;
	}

	return 0;
}

void printMap(Map *map) {
	// top border
	printf("+");
	for (int i=0; i<map->width; i++)
		printf("-");
	printf("+\n");

	for (int i=0; i<map->height; i++) {
		printf("|");
		printf("%s", *(map->map+i));
		printf("|\n");
	}

	// bottom border
	printf("+");
	for (int i=0; i<map->width; i++)
		printf("-");
	printf("+\n");

	// extras
	printf("Width: %d, Height: %d\n", map->width, map->height);
}