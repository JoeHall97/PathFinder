#include <stdlib.h>
#include <string.h>

#include "pathfinder.h"

////////////////////////////////////////////////////
// Map Functions
////////////////////////////////////////////////////

/// @brief Get's the dimension's of a map file.
/// @param fp A file pointer to the map file.
/// @param map The map object to set the map's dimensions.
/// @return 0 if successful, -1 if not
int map_get_dimensions(FILE *fp, Map *map) {
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
    // increase row size for new line characters & in case there is any
    // white-space
    char row[mapWidth + 10];

    while (fgets(row, (mapWidth + 10) * sizeof(char *), fp)) {
        if (row[0] != '|' && row[0] != '+')
            break;
        ++mapHeight;
    }

    map->width = mapWidth - 2;
    map->height = mapHeight - 2;
    rewind(fp);
    return 0;
}

int map_read_file(FILE *fp, Map *map) {
    map_get_dimensions(fp, map);
    // increase row size for new line characters & in case there is any
    // white-space
    char row[map->width + 10];
    int i = 0;

    map->map = (char **) malloc(map->height * sizeof(char *));

    // ignore first row, as it just contains boundary marks
    fgets(row, sizeof(row), fp);

    while (fgets(row, (map->width + 10) * sizeof(row), fp) && i < map->height) {
        map->map[i] = (char *) malloc(map->width * sizeof(char));

        for (int j = 0; j < map->width; j++) {
            map->map[i][j] = row[j + 1];
        }

        ++i;
    }

    return 0;
}

void map_print(const Map *map) {
    // top border
    printf("+");
    for (int i = 0; i < map->width; i++)
        printf("-");
    printf("+\n");

    for (int i = 0; i < map->height; i++) {
        printf("|");
        printf("%s", *(map->map + i));
        printf("|\n");
    }

    // bottom border
    printf("+");
    for (int i = 0; i < map->width; i++)
        printf("-");
    printf("+\n");

    // extras
    printf("Width: %d, Height: %d\n", map->width, map->height);
}

Position *map_get_char_position(const Map *map, const int *c) {
    Position *pos = malloc(sizeof(Position));

    for (int i = 0; i < map->height; i++) {
        for (int j = 0; j < map->width; j++) {
            if (map->map[i][j] == *c) {
                pos->y = i;
                pos->x = j;
                return pos;
            }
        }
    }

    pos->x = -1;
    pos->y = -1;
    return pos;
}

Position *map_get_current_position(const Position *start, const Path *path) {
    Position *res = malloc(sizeof(Position));
    res->x = start->x;
    res->y = start->y;

    for (int i = 0; i < path->len; i++) {
        switch (path->path[i]) {
            case NORTH:
                res->y--;
                break;
            case SOUTH:
                res->y++;
                break;
            case EAST:
                res->x++;
                break;
            case WEST:
                res->x--;
                break;
            default:
                res->x = -1;
                res->y = -1;
                return res;
        }
    }

    return res;
}

////////////////////////////////////////////////////
// Path List Functions
////////////////////////////////////////////////////

PathList *pathlist_append_item(PathList *list, Path *item) {
    if (list == NULL)
        return NULL;

    if (list->item == NULL) {
        list->item = item;
        return list;
    }

    PathList *newNode = malloc(sizeof(PathList));
    newNode->item = item;
    newNode->next = NULL;

    PathList **temp = &list;
    while ((*temp)->next) {
        temp = &(*temp)->next;
    }

    (*temp)->next = newNode;
    return list;
}

PathList *pathlist_push_item(PathList *list, Path *item) {
    PathList *newHead = (PathList *) malloc(sizeof(PathList));
    newHead->item = item;
    newHead->next = list;

    return newHead;
}

Path *pathlist_pop_item(PathList **list) {
    if ((*list) == NULL)
        return NULL;

    Path *res = (*list)->item;
    list = &(*list)->next;
    return res;
}

void pathlist_print(PathList *list) {
    if (list == NULL)
        return;
    PathList *temp = list;

    while (temp->next != NULL) {
        printf("LEN: %d\n", temp->item->len);
        temp = temp->next;
    }
    printf("LEN: %d\n", temp->item->len);
}
