import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

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
            _selectedSearchType = SEARCHTYPE.DEPTHFIRST;
            pathFinder.findPathInMap("../Maps/map1.txt");
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
        Map initMap = readInMap(fileName);
        switch(_selectedSearchType) {
            case ASTAR:
            case BESTFIRST:
                heuristicSearch(initMap);
                break;
            case BREADTHFIRST:
                breadthFirstSearch(initMap);
                break;
            case DEPTHFIRST:
            default:
                depthFirstSearch(initMap);
                break;
        }
    }

    private void depthFirstSearch(Map initMap) {
        Stack<Map> mapFrontier = new Stack<Map>();
        List<int[]> vistedPositions = new ArrayList<int[]>();
        int[] goalPosition = initMap.getGoalPosition();
        Map nextMap;
        _numExpansions = 0;

        mapFrontier.add(initMap);
        vistedPositions.add(initMap.getCurrentPosition());

        while((nextMap = mapFrontier.pop()) != null) {
            ++_numExpansions;
            Map[] tempMaps = expandMapState(nextMap, vistedPositions);
            for (Map m : tempMaps) {
                if(m != null) {
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        m.printPath();
                        System.out.println("NUMBER OF EXPANSIONS: " + _numExpansions);
                        System.out.println("LENGTH OF PATH: " + m.getPath().size());
                        return;
                    }
                    vistedPositions.add(m.getCurrentPosition());
                    mapFrontier.add(m);
                }
            }
        }
        System.out.println("NO PATH TO GOAL FOUND.");
    }

    private void breadthFirstSearch(Map initMap) {
        Queue<Map> mapFrontier = new LinkedList<Map>();
        List<int[]> vistedPositions = new ArrayList<int[]>();
        int[] goalPosition = initMap.getGoalPosition();
        Map nextMap;
        _numExpansions = 0;

        mapFrontier.add(initMap);
        vistedPositions.add(initMap.getCurrentPosition());

        while((nextMap = mapFrontier.poll()) != null) {
            ++_numExpansions;
            Map[] tempMaps = expandMapState(nextMap, vistedPositions);
            for(Map m : tempMaps) {
                if(m != null) {
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        m.printPath();
                        System.out.println("NUMBER OF EXPANSIONS: " + _numExpansions);
                        System.out.println("LENGTH OF PATH: " + m.getPath().size());
                        return;
                    }
                    vistedPositions.add(m.getCurrentPosition());
                    mapFrontier.add(m);
                }
            }
        }
        System.out.println("NO PATH TO GOAL FOUND.");
    }

    private void heuristicSearch(Map initMap) {
        Comparator<Map> mapComparitor = new Comparator<Map>() {
            @Override
            public int compare(Map map1, Map map2) {
                return _selectedSearchType == SEARCHTYPE.BESTFIRST ? heuristic(map1) - heuristic(map2) : 
                (heuristic(map1)+map1.getPath().size()) - (heuristic(map2)+map2.getPath().size());
            }

            private int heuristic(Map map) {
                int[] g = map.getGoalPosition();
                int[] c = map.getCurrentPosition();
                int x = (c[0] - g[0]) * (c[0] - g[0]);
                int y = (c[1] - g[1]) * (c[1] - g[1]);
                return (int)Math.sqrt(x+y);
            }
        };

        PriorityQueue<Map> mapFrontier = new PriorityQueue<Map>(mapComparitor);
        List<int[]> vistedPositions = new ArrayList<int[]>();
        int[] goalPosition = initMap.getGoalPosition();
        Map nextMap;
        _numExpansions = 0;

        mapFrontier.add(initMap);
        vistedPositions.add(initMap.getCurrentPosition());

        while((nextMap = mapFrontier.poll()) != null) {
            ++_numExpansions;
            Map[] tempMaps = expandMapState(nextMap, vistedPositions);
            for(Map m : tempMaps) {
                if(m != null) {
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        m.printPath();
                        System.out.println("NUMBER OF EXPANSIONS: " + _numExpansions);
                        System.out.println("LENGTH OF PATH: " + m.getPath().size());
                        return;
                    }
                    vistedPositions.add(m.getCurrentPosition());
                    mapFrontier.add(m);
                }
            }
        }
        System.out.println("NO PATH TO GOAL FOUND.");
    }

    private Map[] expandMapState(Map map, List<int[]> vistedPositions) {
        Map[] expandedMaps = new Map[4];
        int[] currentPosition = map.getCurrentPosition();
        List<String> currentMap = map.getMap();

        if(currentPosition[0]-1 > 0 && !vistedPositions.parallelStream().anyMatch(a -> Arrays.equals(a, new int[] {currentPosition[0]-1,currentPosition[1]})) && 
        (currentMap.get(currentPosition[0]-1).charAt(currentPosition[1]) == ' ' ||
            currentMap.get(currentPosition[0]-1).charAt(currentPosition[1]) == 'G')) {
                expandedMaps[0] = new Map(map);
                expandedMaps[0].addToPath(Map.DIRECTION.NORTH);
        }
        if(currentPosition[0]+1 < currentMap.size() && !vistedPositions.parallelStream().anyMatch(a -> Arrays.equals(a, new int[] {currentPosition[0]+1,currentPosition[1]})) && 
        (currentMap.get(currentPosition[0]+1).charAt(currentPosition[1]) == ' ' ||
            currentMap.get(currentPosition[0]+1).charAt(currentPosition[1]) == 'G')) {
                expandedMaps[1] = new Map(map);
                expandedMaps[1].addToPath(Map.DIRECTION.SOUTH);
        }
        if(currentPosition[1]-1 > 0 && !vistedPositions.parallelStream().anyMatch(a -> Arrays.equals(a, new int[] {currentPosition[0],currentPosition[1]-1})) && 
        (currentMap.get(currentPosition[0]).charAt(currentPosition[1]-1) == ' ' ||
            currentMap.get(currentPosition[0]).charAt(currentPosition[1]-1) == 'G')) {
                expandedMaps[2] = new Map(map);
                expandedMaps[2].addToPath(Map.DIRECTION.WEST);
        }
        if(currentPosition[1]+1 < currentMap.get(currentPosition[0]).length() && !vistedPositions.parallelStream().anyMatch(a -> Arrays.equals(a, new int[] {currentPosition[0],currentPosition[1]+1})) && 
        (currentMap.get(currentPosition[0]).charAt(currentPosition[1]+1) == ' ' ||
            currentMap.get(currentPosition[0]).charAt(currentPosition[1]+1) == 'G')) {
                expandedMaps[3] = new Map(map);
                expandedMaps[3].addToPath(Map.DIRECTION.EAST);
        }

        return expandedMaps;
    }

    private Map readInMap(String fileName) {
        try{

            File mapFile = new File(fileName);
            Scanner scanner = new Scanner(mapFile);
            
            List<String> mapData = new ArrayList<>();
            while(scanner.hasNextLine()) {
                mapData.add(scanner.nextLine());
            }

            scanner.close();
            
            return new Map(mapData,new ArrayList<Map.DIRECTION>());
        } catch(FileNotFoundException fnfe) {
            System.err.println("An error occured reading in the map file.");
            fnfe.printStackTrace();
            return null;
        }
    }
}