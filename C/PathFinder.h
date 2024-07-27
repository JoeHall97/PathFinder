#pragma once

#include <stdio.h>

#define START_CHARACTER 'S'
#define GOAL_CHARACTER 'G'

/////////////////////////////////////////////////////
// TYPES 
////////////////////////////////////////////////////

/// @brief The caridinal directions that a ship can move in.
typedef enum {
    NORTH,
    SOUTH,
    EAST,
    WEST
} Direction;
/// @brief A set of x,y coordinates.
typedef struct {
    int x;
    int y;
} Position;
/// @brief A map, containing both a 2d string representing the map and a path the the ship has taken. 
typedef struct {
    char **map;
    int height;
    int width;
    Direction *path;
    int pathLength;
} Map;

/// @brief A linked list that contains a list of Map objects.
typedef struct MapList_S {
    Map *item;
    struct MapList_S *next;
} MapList;

////////////////////////////////////////////////////
// FUNCTIONS 
////////////////////////////////////////////////////

/// @brief Reads in a map object from a file pointer.
/// @param fp The file pointer to read in the map from.
/// @param map The map object to read into.
/// @return The created map object.
int map_read_file(FILE *fp, Map* map);
/// @brief Prints out the given map to stdout.
/// @param map 
void map_print(Map *map);
/// @brief Gets the position of the given char in the provided map.
/// @param map 
/// @param c 
/// @return The x,y coordinates of the given char position.
Position *map_get_char_position(Map *map, int *c);
/// @brief Gets the current position based on the start position and the given path.
/// @param startPos The start position
/// @param path A list of directions that have been taken.
/// @param pathLength The length of the path list.
/// @return The x,y coordinates of the current position.
Position *map_get_current_position(Position *start, Direction *path, int *pathLength);

/// @brief Adds the given item to the end of the MapList.
/// @param list The list to add the item to.
/// @param item The item to add to the list.
void maplist_append_item(MapList *list, Map *item);
/// @brief Adds the given item to the start of the list.
/// @param list The list to add the item to.
/// @param item The item to add to the list.
void maplist_push_item(MapList *list, Map *item);
/// @brief Removes the top item from the list and returns it.
/// @param list The list to remove the item from.
/// @return The item remove from the list.
Map *maplist_pop_item(MapList *list);