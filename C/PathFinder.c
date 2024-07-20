#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "PathFinder.h"

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

/* readMap: reads in a map object from a file pointer */
Map readMap(FILE *fp) {
    Map newMap = {
        .map = NULL,
        .height = 0,
        .width = 0,
        .path = NULL,
        .pathLength = 0
    };

    getMapDimensions(fp, &newMap);
	// increase row size for new line characters & in case there is any white-space
	char row[newMap.width+10];
	int i = 0;

	newMap.map = (char **) malloc(newMap.height * sizeof(char *));

	// ignore first row, as it's just contains boundary marks
	fgets(row, sizeof(row), fp);

	while (fgets(row, (newMap.width+10)*sizeof(row), fp) && i<newMap.height) {
		newMap.map[i] = (char *) malloc(newMap.width * sizeof(char));

		for (int j=0; j<newMap.width; j++) {
			newMap.map[i][j] = row[j+1];
		}

		++i;
	}

	return newMap;
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