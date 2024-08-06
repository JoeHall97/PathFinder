#pragma once

#include <stdbool.h>
#include <stdio.h>

#define START_CHARACTER 'S'
#define GOAL_CHARACTER 'G'

/////////////////////////////////////////////////////
// TYPES
////////////////////////////////////////////////////

/// @brief The caridinal directions that a ship can move in.
typedef enum
{
    NORTH,
    SOUTH,
    EAST,
    WEST
} Direction;

/// @brief A set of x,y coordinates.
typedef struct
{
    int x;
    int y;
} Position;

/// @brief A map, a 2d string representing a map.
typedef struct
{
    char **map;
    int height;
    int width;
} Map;

typedef struct
{
    Direction *path;
    int len;
    Position *currentPoisition;
} Path;

/// @brief A linked list that contains a list of Map objects.
typedef struct maplist_s
{
    Map *item;
    struct maplist_s *next;
} MapList;

/// @brief A linked list that contains a list of Path objects.
typedef struct pathlist_s {
    Path *item;
    struct pathlist_s *next;
} PathList;

/// @brief A 2d array of bools that are set based on the visited positions.
typedef struct
{
    bool **boolMap;
    int width;
    int height;
} VisitedMap;

////////////////////////////////////////////////////
// FUNCTIONS
////////////////////////////////////////////////////

/// @brief Reads in a map object from a file pointer.
/// @param fp The file pointer to read in the map from.
/// @param map The map object to read into.
/// @return The created map object.
int map_read_file(FILE *fp, Map *map);
/// @brief Prints out the given map to stdout.
/// @param map
void map_print(const Map *map);
/// @brief Gets the position of the given char in the provided map.
/// @param map
/// @param c
/// @return The x,y coordinates of the given char position.
Position *map_get_char_position(const Map *map, const int *c);
/// @brief Gets the current position based on the start position and the given
/// path.
/// @param startPos The start position
/// @param path A list of directions that have been taken.
/// @return The x,y coordinates of the current position.
Position *map_get_current_position(const Position *start, const Path *path);
/// @brief Returns a list of valid maps based on the current map.
/// @param map
/// @param visited
/// @return A list of valid map expansions.
MapList *map_expand(const Map *map, const VisitedMap *visited);

/// @brief Adds the given item to the end of the list.
/// @param list The list to add the item to.
/// @param item The item to add to the list.
void pathlist_append_item(PathList *list, Path *item);
/// @brief Adds the given item to the start of the list.
/// @param list The list to add the item to.
/// @param item The item to add to the list.
void pathlist_push_item(PathList *list, Path *item);
/// @brief Removes the top item from the list and returns it.
/// @param list The list to remove the item from.
/// @return The item remove from the list.
Map *pathlist_pop_item(PathList *list);
/// @brief Print the list to stdout.
/// @param list 
void pathlist_print(PathList *list);