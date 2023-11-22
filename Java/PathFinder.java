import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Comparator;
import java.util.HashSet;

public class PathFinder {
    public enum SEARCHTYPE {
        DEPTHFIRST,
        BREADTHFIRST,
        BESTFIRST,
        ASTAR
    }

    private static int _numExpansions;
    private static SEARCHTYPE _selectedSearchType;

    public static void main(String args[]) {
        PathFinder pathFinder = new PathFinder();
        if(args.length < 2) {
            _selectedSearchType = SEARCHTYPE.BESTFIRST;
            pathFinder.findPathInMap("../Maps/map3.txt");
        } else if(args.length == 2) {
            try {
                _selectedSearchType = SEARCHTYPE.valueOf(args[1].toUpperCase());
                pathFinder.findPathInMap(args[0]);
            } catch(IllegalArgumentException exception) {
                System.err.print(args[1] + " is not a valid search type. The valid search types are: [depthfirst, breadthfirst, bestfirst, astar]");
            }
        } else {
            System.out.println("USAGE: java PathFinder\nOR\njava PathFinder <pathToMapFile> <searchAlgorithm>");
        }
    }

    private void findPathInMap(String fileName) {
        List<String> mapData = readInMapData(fileName);
        switch(_selectedSearchType) {
            case ASTAR:
            case BESTFIRST:
                Map initHeuristicMap = new Map(mapData, new ArrayList<Map.DIRECTION>(), true);
                heuristicSearch(initHeuristicMap);
                break;
            case BREADTHFIRST:
                Map initBreadthMap = new Map(mapData, new ArrayList<Map.DIRECTION>(), false);
                breadthFirstSearch(initBreadthMap);
                break;
            case DEPTHFIRST:
            default:
                Map initDepthMap = new Map(mapData, new ArrayList<Map.DIRECTION>(), false);
                depthFirstSearch(initDepthMap);
                break;
        }
    }

    private void depthFirstSearch(Map initMap) {
        Stack<Map> mapFrontier = new Stack<Map>();
        HashSet<String> vistedPositions = new HashSet<String>();
        int[] goalPosition = initMap.getGoalPosition();
        Map nextMap;
        _numExpansions = 0;

        mapFrontier.add(initMap);
        vistedPositions.add(initMap.getCurrentPositionString());

        while((nextMap = mapFrontier.pop()) != null) {
            ++_numExpansions;
            Map[] tempMaps = expandMapState(nextMap, vistedPositions);
            for (Map m : tempMaps) {
                if(m != null) {
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        System.out.print("PATH: ");
                        m.printPath();
                        System.out.println("NUMBER OF EXPANSIONS: " + _numExpansions);
                        System.out.println("LENGTH OF PATH: " + m.getPath().size());
                        return;
                    }
                    vistedPositions.add(m.getCurrentPositionString());
                    mapFrontier.add(m);
                }
            }
        }
        System.out.println("NO PATH TO GOAL FOUND.");
    }

    private void breadthFirstSearch(Map initMap) {
        Queue<Map> mapFrontier = new LinkedList<Map>();
        HashSet<String> vistedPositions = new HashSet<String>();
        int[] goalPosition = initMap.getGoalPosition();
        Map nextMap;
        _numExpansions = 0;

        mapFrontier.add(initMap);
        vistedPositions.add(initMap.getCurrentPositionString());

        while((nextMap = mapFrontier.poll()) != null) {
            ++_numExpansions;
            Map[] tempMaps = expandMapState(nextMap, vistedPositions);
            for(Map m : tempMaps) {
                if(m != null) {
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        System.out.print("PATH: ");
                        m.printPath();
                        System.out.println("NUMBER OF EXPANSIONS: " + _numExpansions);
                        System.out.println("LENGTH OF PATH: " + m.getPath().size());
                        return;
                    }
                    vistedPositions.add(m.getCurrentPositionString());
                    mapFrontier.add(m);
                }
            }
        }
        System.out.println("NO PATH TO GOAL FOUND.");
    }

    private void heuristicSearch(Map initMap) {
        DoublePriorityQueue<Map> mapFrontier = new DoublePriorityQueue<Map>();
        HashSet<String> vistedPositions = new HashSet<String>();
        int[] goalPosition = initMap.getGoalPosition();
        Map nextMap;
        _numExpansions = 0;

        mapFrontier.push(initMap, getMapPriority(initMap));
        vistedPositions.add(initMap.getCurrentPositionString());

        while((nextMap = mapFrontier.pop()) != null) {
            ++_numExpansions;
            Map[] tempMaps = expandMapState(nextMap, vistedPositions);
            for(Map m : tempMaps) {
                if(m != null) {
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        System.out.print("PATH: ");
                        m.printPath();
                        System.out.println("NUMBER OF EXPANSIONS: " + _numExpansions);
                        System.out.println("LENGTH OF PATH: " + m.getPath().size());
                        return;
                    }
                    vistedPositions.add(m.getCurrentPositionString());
                    mapFrontier.push(m,getMapPriority(m));
                }
            }
        }
        System.out.println("NO PATH TO GOAL FOUND.");
    }

    private double getMapPriority(Map map) {
        return _selectedSearchType == SEARCHTYPE.BESTFIRST ? map.getHeuristic() : map.getHeuristicAndCost();
    }

    private Map[] expandMapState(Map map, HashSet<String> vistedPositions) {
        Map[] expandedMaps = new Map[4];
        int[] currentPosition = map.getCurrentPosition();

        if(map.checkPositionForExpansion(currentPosition[0]-1,currentPosition[1]) && !vistedPositions.contains(positionToString(currentPosition[0]-1,currentPosition[1]))) {
                expandedMaps[0] = new Map(map);
                expandedMaps[0].addToPath(Map.DIRECTION.NORTH);
        }
        if(map.checkPositionForExpansion(currentPosition[0]+1,currentPosition[1]) && !vistedPositions.contains(positionToString(currentPosition[0]+1,currentPosition[1]))) {
                expandedMaps[1] = new Map(map);
                expandedMaps[1].addToPath(Map.DIRECTION.SOUTH);
        }
        if(map.checkPositionForExpansion(currentPosition[0],currentPosition[1]-1) && !vistedPositions.contains(positionToString(currentPosition[0],currentPosition[1]-1))) {
                expandedMaps[2] = new Map(map);
                expandedMaps[2].addToPath(Map.DIRECTION.WEST);
        }
        if(map.checkPositionForExpansion(currentPosition[0],currentPosition[1]+1) && !vistedPositions.contains(positionToString(currentPosition[0],currentPosition[1]+1))) {
                expandedMaps[3] = new Map(map);
                expandedMaps[3].addToPath(Map.DIRECTION.EAST);
        }

        return expandedMaps;
    }

    private String positionToString(int positionX, int positionY)
    {
        return positionX + "," + positionY;
    }

    private List<String> readInMapData(String fileName) {
        try{

            File mapFile = new File(fileName);
            Scanner scanner = new Scanner(mapFile);
            
            List<String> mapData = new ArrayList<>();
            while(scanner.hasNextLine()) {
                mapData.add(scanner.nextLine());
            }

            scanner.close();
            
            return mapData;
        } catch(FileNotFoundException fnfe) {
            System.err.println("An error occured reading in the map file.");
            fnfe.printStackTrace();
            return null;
        }
    }
}