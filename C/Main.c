#include <stdlib.h>

#include "PathFinder.h"

int main(int argc, char *argv[])
{
	FILE *fp;
	char *prog = argv[0]; /* program name for errors */
	Map map = {};
	PathList paths = {
		.item = NULL,
		.next = NULL,
	};
	int sChar = START_CHARACTER;
	int gChar = GOAL_CHARACTER;

	// create a bool map with the same dimensions as the actual map & use that to
	// track visted positions

	if (argc == 1)
	{
		if ((fp = fopen("../Maps/map.txt", "r")) == NULL)
		{
			fprintf(stderr, "%s: can't open %s\n", prog, "../Maps/map.txt");
			exit(1);
		}
		else
		{
			map_read_file(fp, &map);
			fclose(fp);
		}
	}
	else
	{
		if ((fp = fopen(*++argv, "r")) == NULL)
		{
			fprintf(stderr, "%s: can't open %s\n", prog, *argv);
			exit(1);
		}
		else
		{
			map_read_file(fp, &map);
			fclose(fp);
		}
	}

	VisitedMap visited = {.width = map.width, .height = map.height};
	visited.boolMap = (bool **) malloc(visited.height * sizeof(bool *));
	for (int i=0;i<visited.height;i++)
		visited.boolMap[i] = (bool *) malloc(visited.width * sizeof(bool));


	Position *startPos = map_get_char_position(&map, &sChar);
	Position *goalPos = map_get_char_position(&map, &gChar);
	Path firstPath = {
		.path = NULL,
		.len = 0,
		.currentPoisition = startPos
	};

	pathlist_push_item(&paths, &firstPath);
	Path secondPath = {
		.path = NULL,
		.len = 1,
		.currentPoisition = startPos
	};
	pathlist_push_item(&paths, &secondPath);
	pathlist_print(&paths);

	exit(0);
}
