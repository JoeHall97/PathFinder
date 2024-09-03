#include <stdlib.h>

#include "pathfinder.h"

void search_map(const Map *map, const Position *start, const Position *goal);

int main(const int argc, char *argv[]) {
    FILE *fp;
    char *prog = argv[0]; /* program name for errors */
    Map map = {};
    const int sChar = START_CHARACTER;
    const int gChar = GOAL_CHARACTER;

    if (argc == 1) {
        if ((fp = fopen("Maps/map.txt", "r")) == NULL) {
            fprintf(stderr, "%s: can't open %s\n", prog, "Maps/map.txt");
            exit(1);
        } else {
            map_read_file(fp, &map);
            fclose(fp);
        }
    } else {
        if ((fp = fopen(*++argv, "r")) == NULL) {
            fprintf(stderr, "%s: can't open %s\n", prog, *argv);
            exit(1);
        } else {
            map_read_file(fp, &map);
            fclose(fp);
        }
    }

    const Position *startPos = map_get_char_position(&map, &sChar);
    const Position *goalPos = map_get_char_position(&map, &gChar);

    search_map(&map, startPos, goalPos);

    exit(0);
}

void search_map(const Map *map, const Position *start, const Position *goal) {
    // allocate needed resources
    PathList *paths = malloc(sizeof(PathList));
    VisitedMap visited = {.width = map->width, .height = map->height};
    visited.boolMap = (bool **) malloc(visited.height * sizeof(bool *));
    for (int i = 0; i < visited.height; i++)
        visited.boolMap[i] = (bool *) malloc(visited.width * sizeof(bool));

    // free up used resources
    free(paths);
    for (int i = 0; i < visited.height; i++)
        free(visited.boolMap[i]);
    free(visited.boolMap);
}
