"""
Created on Wed Aug 21 17:11:14 2019

@author: Joseph Hall
"""
from math import sqrt
from sys import argv
import time
from PriorityQueue import PriorityQueue

directions: dict[str, int] = {"north":-1, "south":1, "east":1, "west":-1}

class Map:
    def __init__(self, map: list[str], path: list[str]) -> None:
        self.map = map
        self.curr_pos = []
        self.path = path
        self.start_pos = self.__getStartPos()
        self.start_pos_str = str(self.start_pos)
        self.goal_pos =  self.__getGoalPos()
        self.goal_pos_str = str(self.goal_pos)
        self.__drawPath()
        self.curr_pos_str = str(self.curr_pos)

    def __str__(self) -> str:
        s = ""
        for m in self.map:
            s += m
        s += f"\nPath:{self.path}\nPath Length:{len(self.path)}\nCurrent Position:{self.curr_pos}\n"
        return s

    def __getStartPos(self) -> int | list[int]:
        pos = 0
        for line in self.map:
            if(line.find('S')!=-1):
                return [pos,line.find('S')]
            pos += 1
        return -1

    def __getGoalPos(self) -> int | list[int]:
        pos = 0
        for line in self.map:
            if(line.find('G')!=-1):
                return [pos,line.find('G')]
            pos += 1
        return -1

    def __drawPath(self) -> None:
        pos = self.start_pos
        if pos == -1:
            print("ERROR: No start position in given map file!")
            return
        for p in self.path:
            if(p=="north" or p=="south"):
                pos[0] += directions[p]
            else:
                pos[1] += directions[p]
            line = self.map[pos[0]]
            self.map[pos[0]] = line[0:pos[1]] + '.' + line[pos[1]+1:len(line)]
        self.curr_pos = pos

    def cost(self) -> int:
        return len(self.path)
    
    def heuristic(self) -> float:
        goal_pos = self.goal_pos
        curr_pos = self.curr_pos
        # √((posX-goalX)^2 + (posY-goalY)^2)
        x_value = (curr_pos[0]-goal_pos[0]) * (curr_pos[0]-goal_pos[0])
        y_value = (curr_pos[1]-goal_pos[1]) * (curr_pos[1]-goal_pos[1])
        return sqrt(x_value+y_value)
    
    def heuristicAndCost(self) -> float:
        return(self.heuristic()+self.cost())
    
    def checkInBounds(self, pos: list[int]) -> bool:
        if pos[1] >= len(self.map[0]) or pos[1] < 0:
            return False
        return not (pos[0] >= len(self.map) or pos[0] < 0)
    
    def checkPositionForExpansion(self, pos: list[int]) -> bool:
        if not self.checkInBounds(pos):
            return False
        return self.map[pos[0]][pos[1]] == ' ' or self.map[pos[0]][pos[1]] == 'G'


def readMap(map_name: str) -> str:
    m = open(map_name,'r')
    map = [line for line in m]
    return map
        
def expand(map: Map, visted_positions: set) -> list[Map]:
    maps = []
    pos = map.curr_pos
    # check north
    if str([pos[0]-1, pos[1]]) not in visted_positions and map.checkPositionForExpansion([pos[0]-1, pos[1]]):
        new_path = map.path.copy()
        new_path.append("north")
        maps.append(Map(map.map.copy(), new_path))
    # check south
    if str([pos[0]+1, pos[1]]) not in visted_positions and map.checkPositionForExpansion([pos[0]+1, pos[1]]):
        new_path = map.path.copy()
        new_path.append("south")
        maps.append(Map(map.map.copy(), new_path))
    # check west
    if str([pos[0], pos[1]-1]) not in visted_positions and map.checkPositionForExpansion([pos[0], pos[1]-1]):
        new_path = map.path.copy()
        new_path.append("west")
        maps.append(Map(map.map.copy(), new_path))
    # check east
    if str([pos[0], pos[1]+1]) not in visted_positions and map.checkPositionForExpansion([pos[0], pos[1]+1]):
        new_path = map.path.copy()
        new_path.append("east")
        maps.append(Map(map.map.copy(), new_path))
    return maps

def searchMap(debug: bool, fileName: str, search_type: str) -> None:
    search_map = Map(readMap(fileName),[])
    maps = [search_map]
    visted_positions = set(search_map.start_pos_str)
    goal_pos = maps[0].goal_pos
    num_expansions = 0
    
    while True:
        if debug:
            print(maps[0])
            print(f"Number of expansions: {str(num_expansions)}")
            time.sleep(0.25)
        
        num_expansions += 1
        for m in expand(maps.pop(0), visted_positions):
            if goal_pos == m.curr_pos:
                # add goal back to map
                goal_line = m.map[goal_pos[0]]
                gl1 = goal_line[0:goal_pos[1]]
                gl2 = goal_line[goal_pos[1]+1:len(goal_line)]
                goal_line = gl1 + 'G' +  gl2
                m.map[goal_pos[0]] = goal_line

                print(m)
                print(f"Number of expansions: {str(num_expansions)}")
                return

            if search_type == "depth-first":
                maps.insert(0,m)
            else:
                maps.append(m)    
            visted_positions.add(m.curr_pos_str)

def searchMapHeuristic(debug: bool, fileName: str, heuristic_type: str) -> None:
    search_map = Map(readMap(fileName),[])
    maps = PriorityQueue()
    visted_positions = set(search_map.start_pos_str)
    goal_pos = search_map.goal_pos
    num_expansions = 0

    if heuristic_type == "best-first":
        priority = search_map.heuristic()         
    else:
        priority = search_map.heuristicAndCost()
    maps.insert(search_map,priority)
    
    while True:
        if debug:
            print(maps[0])
            print(f"Number of expansions: {str(num_expansions)}")
            time.sleep(0.25)
        
        num_expansions += 1
        for m in expand(maps.pop(), visted_positions):
            if goal_pos == m.curr_pos:
                # add goal back to map
                goal_line = m.map[goal_pos[0]]
                gl1 = goal_line[0:goal_pos[1]]
                gl2 = goal_line[goal_pos[1]+1:len(goal_line)]
                goal_line = gl1 + 'G' +  gl2
                m.map[goal_pos[0]] = goal_line

                print(m)
                print(f"Number of expansions: {str(num_expansions)}")
                return

            if heuristic_type == "best-first":
                priority = m.heuristic() 
            else:
                priority = m.heuristicAndCost()
            maps.insert(m,priority)
            visted_positions.add(m.curr_pos_str)

def main() -> None:
    if len(argv) == 1:
        searchMapHeuristic(False,"../Maps/map3.txt","best-first")
    else:
        alogrithms = set(["a-star", "best-first", "breadth-first", "depth-first"])
        last_arg_pos = len(argv)-1
        if argv[last_arg_pos] not in alogrithms:
            print(f"Search type {argv[last_arg_pos]} is not a valid search type.")
            return
        if argv[last_arg_pos] in alogrithms[2:]:
            searchMap(len(argv) == 4, argv[last_arg_pos-1], argv[last_arg_pos])
        else:
            searchMapHeuristic(len(argv) == 4, argv[last_arg_pos-1], argv[last_arg_pos])

if __name__ == "__main__":  
    main()