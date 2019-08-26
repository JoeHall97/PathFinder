#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Aug 21 17:11:14 2019

@author: Joseph Hall
"""
from math import pow, sqrt
#from math import sqrt

directions = {"north":-1, "south":1, "east":1, "west":-1}

class Map:
    def __init__(self,map,path):
        self.map = map
        self.path = path
        self.drawPath()
        
    def drawPath(self):
        pos = start(self)
        for p in self.path:
            if(p=="north" or p=="south"):
                pos[0] += directions[p]
            else:
                pos[1] += directions[p]
            line = list(self.map[pos[0]])
            line[pos[1]] = '.'
            self.map[pos[0]] = "".join(line)

def readMap(mapName):
    m = open(mapName,'r')
    map = [line for line in m]
    return map

def printMap(map):
    for m in map.map:
        print(m.strip())
    print(map.path)
        
def position(map):
    pos = start(map)
    for p in map.path:
       if(p=="north" or p=="south"):
           pos[0] += directions[p]
       else:
           pos[1] += directions[p]
    return pos

def start(map):
    pos = 0
    for line in map.map:
        if(line.find('S')!=-1):
            return [pos,line.find('S')]
        pos+=1
    return -1

def goal(map):
    pos = 0
    for line in map.map:
        if(line.find('G')!=-1):
            return [pos,line.find('G')]
        pos+=1
    return -1

def cost(map):
    return len(map.path)

def heuristic(map):
    goalPos = goal(map)
    currPos = position(map)
    # √(posX-goalX)^2 + (posY-goalY)^2
    return (sqrt(pow(currPos[0]-goalPos[0],2)+pow(currPos[1]-goalPos[1],2)))

def expand(map):
    maps = []
    pos = position(map)
    # check north
    if(pos[0]-1>0 and map.map[pos[0]-1][pos[1]]==' '):
        newPath = map.path.copy()
        newPath.append("north")
        maps.append(Map(map.map.copy(),newPath))
    # check south
    if(pos[0]+1<len(map.map) and map.map[pos[0]+1][pos[1]]==' '):
        newPath = map.path.copy()
        newPath.append("south")
        maps.append(Map(map.map.copy(),newPath))
    # check west
    if(pos[1]-1>0 and map.map[pos[0]][pos[1]-1]==' '):
        newPath = map.path.copy()
        newPath.append("west")
        maps.append(Map(map.map.copy(),newPath))
    # check east
    if(pos[1]+1<len(map.map[pos[0]]) and map.map[pos[0]][pos[1]+1]==' '):
        newPath = map.path.copy()
        newPath.append("east")
        maps.append(Map(map.map.copy(),newPath))
    return maps

maps = [Map(readMap('map1.txt'),[])]
printMap(maps[0])
for e in expand(maps[0]):
    printMap(e)
