#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Aug 21 17:11:14 2019

@author: Joseph Hall
"""
from math import pow, sqrt
import sys
import time

directions = {"north":-1, "south":1, "east":1, "west":-1}
debug = True

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
    # √((posX-goalX)^2 + (posY-goalY)^2)
    xValue = currPos[0]-goalPos[0]
    yValue = currPos[1]-goalPos[1]
    return sqrt(xValue**2+yValue**2)

def heuristicAndCost(map):
    return(heuristic(map)+cost(map))

def expand(map):
    maps = []
    pos = position(map)
    # check north
    if(pos[0]-1>0 and (map.map[pos[0]-1][pos[1]]==' ' or map.map[pos[0]-1][pos[1]]=='G')):
        newPath = map.path.copy()
        newPath.append("north")
        maps.append(Map(map.map.copy(),newPath))
    # check south
    if(pos[0]+1<len(map.map) and (map.map[pos[0]+1][pos[1]]==' '  or map.map[pos[0]+1][pos[1]]=='G')):
        newPath = map.path.copy()
        newPath.append("south")
        maps.append(Map(map.map.copy(),newPath))
    # check west
    if(pos[1]-1>0 and (map.map[pos[0]][pos[1]-1]==' ' or map.map[pos[0]][pos[1]-1]=='G')):
        newPath = map.path.copy()
        newPath.append("west")
        maps.append(Map(map.map.copy(),newPath))
    # check east
    if(pos[1]+1<len(map.map[pos[0]]) and (map.map[pos[0]][pos[1]+1]==' ' or map.map[pos[0]][pos[1]+1]=='G')):
        newPath = map.path.copy()
        newPath.append("east")
        maps.append(Map(map.map.copy(),newPath))
    return maps

def bestFirst(fileName):
    maps = [Map(readMap(fileName),[])]
    # maps that have been expanded
    vistedMaps = [Map(readMap(fileName),[])]
    goalPos = goal(maps[0])
    numExpansions = 0
    # loop through til the goal is reached
    while True:
        if(debug):
            printMap(maps[0])
            print("Number of expansions: " + str(numExpansions))
            #time.sleep(0.5)
        numExpansions += 1
        for m in expand(maps.pop(0)):
            # if one of the expanded states has reached the goal
            if(goalPos==position(m)):
                printMap(m)
                print("Number of expansions: " + str(numExpansions))
                return
            if m not in vistedMaps:
                maps.append(m)
                vistedMaps.append(m)
        maps = sorted(maps,key=heuristic)
    
def aStar(fileName):
    maps = [Map(readMap(fileName),[])]
    # maps that have been visted
    vistedPaths = [[]]
    goalPos = goal(maps[0])
    numExpansions = 0
    # loop through til the goal is reached
    while True:
        if(debug):
            printMap(maps[0])
            print("Number of expansions: " + str(numExpansions))
            #time.sleep(1)
        numExpansions += 1
        for m in expand(maps.pop(0)):
            # if one of the expanded states has reached the goal
            if(goalPos==position(m)):
                printMap(m)
                print("Number of expansions: " + str(numExpansions))
                return
            if m.path not in vistedPaths:
                maps.append(m)
                vistedPaths.append(m.path)
        maps = sorted(maps,key=heuristicAndCost)
        #print(heuristicAndCost(maps[0]))
        #print(min([heuristicAndCost(m) for m in maps]))

def main():
    aStar('map2.txt')
        
main()