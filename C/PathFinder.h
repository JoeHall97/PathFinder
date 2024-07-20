#pragma once

#include <stdio.h>

typedef enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
} Direction;

typedef struct Map {
    char **map;
    int height;
    int width;
    Direction *path;
    int pathLength;
} Map;

Map readMap(FILE *fp);

void printMap(Map *map);