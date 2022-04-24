# PathFinder
A python and java program that takes in a map file and finds the shortest path between the start and the goal using various techniques.
Maps consist of a border around the map, 'X's marking land, 'S' for the starting position and 'G' for the finishing position (or goal). An example of this would be the following map:
```
+---------------+
|            GX |
|   X       XXX |
|  XXXX   XXXXX |
| XXXX    X  XX |
|   SX          |
+---------------+
```
The programs contains four searching algorithms that can be selected via the following program arguments: *depth-first*, *breadth-first*, *best-first*, *a-star*
