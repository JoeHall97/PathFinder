#include <stdlib.h>

#include "PathFinder.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	char *prog = argv[0];  /* program name for errors */
	
	Map map = {};
	MapList mapList = {
		.item = NULL,
		.next = NULL,
	};
	int sChar = START_CHARACTER;
	int gChar = GOAL_CHARACTER;

	if (argc == 1)  {
		if ((fp = fopen("../Maps/map.txt", "r")) == NULL) {
			fprintf(stderr, "%s: can't open %s\n", prog, "../Maps/map.txt");
			exit(1);
		} else {
			map_read_file(fp, &map);
			fclose(fp);
		}
	}
	else {
		if ((fp = fopen(*++argv, "r")) == NULL) {
			fprintf(stderr, "%s: can't open %s\n", prog, *argv);
			exit(1);
		} else {
			map_read_file(fp, &map);
			fclose(fp);
		}
	}

	if (ferror(stdout)) {
		fprintf(stderr, "%s: error writing stdout\n", prog);
		exit(2);
	}

	maplist_append_item(&mapList, &map);

	Position *startPos = map_get_char_position(&map, &sChar);
	Position *goalPos = map_get_char_position(&map, &gChar);

	exit(0);
}
