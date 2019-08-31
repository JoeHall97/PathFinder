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
    xValue = pow((currPos[0]-goalPos[0]),2)
    yValue = pow((currPos[1]-goalPos[1]),2)
    return sqrt(xValue+yValue)

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

def searchMap(debug,fileName,searchType):
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
            print("Cost: " + str(cost(maps[0])))
            time.sleep(0.5)
        numExpansions += 1
        for m in expand(maps.pop(0)):
            # if one of the expanded states has reached the goal
            if(goalPos==position(m)):
                printMap(m)
                print("Number of expansions: " + str(numExpansions))
                print("Cost: " + str(cost(m)))
                return
            if m not in vistedMaps:
                if(searchType in ["a-star", "best-first", "breadth-first"]):
                    maps.append(m)
                elif(searchType=="depth-first"):
                    #prepend m to the list of maps
                    maps = [m] + maps
                vistedMaps.append(m)
        if(searchType=="a-star"):
            maps = sorted(maps,key=heuristicAndCost)
        elif(searchType=="best-first"):
            maps = sorted(maps,key=heuristic)            

def main():
    alogrithms = ["a-star", "best-first", "breadth-first", "depth-first"]
    if len(sys.argv)!=4 and len(sys.argv)!=3:
        print(len(sys.argv))
        print("Usage: python PathFinder <debug> <filename> <algorithm>")
        sys.exit(1)
    if(len(sys.argv)==4 and sys.argv[3] not in alogrithms) or (len(sys.argv)==3 and sys.argv[2] not in alogrithms):
        print(sys.argv[2] + " is not a valid searching alogrithm")
        print("Valid search alogrithms are: a-star, best-first, breadth-first, depth-first")
        sys.exit(1)
    if len(sys.argv)==4:
        searchMap(True,str(sys.argv[2]),str(sys.argv[3]))
    else:
        searchMap(False,str(sys.argv[1]),str(sys.argv[2]))
    #searchMap(False,"map.txt",'best-first')
        
main()