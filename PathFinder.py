#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Aug 21 17:11:14 2019

@author: Joseph Hall
"""
from math import sqrt
import sys
# import time

directions = {"north":-1, "south":1, "east":1, "west":-1}

class Map:
    def __init__(self,map,path):
        self.map = map
        self.curr_pos = []
        self.path = path
        self.drawPath()
        
    def drawPath(self):
        pos = start(self)
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

def readMap(map_name):
    m = open(map_name,'r')
    map = [line for line in m]
    return map

def printMap(map):
    for m in map.map:
        print(m.strip())
    print(map.path)
    print(f"Current position: {map.curr_pos}")
        
def start(map):
    pos = 0
    for line in map.map:
        if(line.find('S')!=-1):
            return [pos,line.find('S')]
        pos += 1
    return -1

def goal(map):
    pos = 0
    for line in map.map:
        if(line.find('G')!=-1):
            return [pos,line.find('G')]
        pos += 1
    return -1

def cost(map):
    return len(map.path)

def heuristic(map):
    goal_pos = goal(map)
    curr_pos = map.curr_pos
    # √((posX-goalX)^2 + (posY-goalY)^2)
    x_value = (curr_pos[0]-goal_pos[0]) * (curr_pos[0]-goal_pos[0])
    y_value = (curr_pos[1]-goal_pos[1]) * (curr_pos[1]-goal_pos[1])
    return sqrt(x_value+y_value)

def heuristicAndCost(map):
    return(heuristic(map)+cost(map))

def expand(map,visted_positions):
    maps = []
    pos = map.curr_pos
    # check north
    if pos[0]-1>0 and [pos[0]-1,pos[1]] not in visted_positions and (map.map[pos[0]-1][pos[1]]==' ' or map.map[pos[0]-1][pos[1]]=='G'):
        new_path = map.path.copy()
        new_path.append("north")
        maps.append(Map(map.map.copy(),new_path))
    # check south
    if pos[0]+1<len(map.map) and [pos[0]+1,pos[1]] not in visted_positions and (map.map[pos[0]+1][pos[1]]==' ' or map.map[pos[0]+1][pos[1]]=='G'):
        new_path = map.path.copy()
        new_path.append("south")
        maps.append(Map(map.map.copy(),new_path))
    # check west
    if pos[1]-1>0 and [pos[0],pos[1]-1] not in visted_positions and (map.map[pos[0]][pos[1]-1]==' ' or map.map[pos[0]][pos[1]-1]=='G'):
        new_path = map.path.copy()
        new_path.append("west")
        maps.append(Map(map.map.copy(),new_path))
    # check east
    if pos[1]+1<len(map.map[pos[0]]) and [pos[0],pos[1]+1] not in visted_positions and (map.map[pos[0]][pos[1]+1]==' ' or map.map[pos[0]][pos[1]+1]=='G'):
        new_path = map.path.copy()
        new_path.append("east")
        maps.append(Map(map.map.copy(),new_path))
    return maps

def searchMap(debug,fileName,search_type):
    if search_type not in ["a-star", "best-first", "breadth-first", "depth-first"]:
        print(f"Search type {search_type} is not a valid search type.")
        return

    search_map = Map(readMap(fileName),[])
    maps = [search_map]
    visted_positions = [start(search_map)]
    goal_pos = goal(maps[0])
    num_expansions = 0
    
    while True:
        if(debug):
            printMap(maps[0])
            print(f"Number of expansions: {str(num_expansions)}")
            print(f"Path Length: {str(cost(maps[0]))}")
            # time.sleep(0.2)
        
        num_expansions += 1
        for m in expand(maps.pop(0),visted_positions):
            if(goal_pos==m.curr_pos):
                # add goal back to map
                goal_line = m.map[goal_pos[0]]
                goal_line = goal_line[0:goal_pos[1]] + 'G' +  goal_line[goal_pos[1]+1:len(goal_line)]
                m.map[goal_pos[0]] = goal_line

                printMap(m)
                print(f"Number of expansions: {str(num_expansions)}")
                print(f"Path Length: {str(cost(m))}")
                return

            if(search_type in ["a-star", "best-first", "breadth-first"]):
                maps.append(m)
            elif(search_type=="depth-first"):
                maps = [m] + maps
            visted_positions.append(m.curr_pos)
        
        if(search_type=="a-star"):
            maps = sorted(maps,key=heuristicAndCost)
        elif(search_type=="best-first"):
            maps = sorted(maps,key=heuristic)            

def main():
    # alogrithms = ["a-star", "best-first", "breadth-first", "depth-first"]
    if len(sys.argv) == 4:
        searchMap(True,str(sys.argv[2]),str(sys.argv[3]))
    elif len(sys.argv) == 3:
        searchMap(False,str(sys.argv[1]),str(sys.argv[2]))
    else:
        searchMap(False,"./map2.txt","a-star")

if __name__ == "__main__":  
    main()