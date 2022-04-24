import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;

public class PathFinder {
    public enum SEARCHTYPE {
        DEPTHFIRST,
        BREADTHFIRST,
        BESTFIRST,
        ASTAR
    }

    private static int _numExpansions;

    public static void main(String args[]) {
        PathFinder pathFinder = new PathFinder();
        if(args.length < 2) {
            pathFinder.findPathInMap("../Maps/map.txt", SEARCHTYPE.DEPTHFIRST);
        } else {
            pathFinder.findPathInMap(args[0], SEARCHTYPE.valueOf(args[1].toUpperCase()));
        }
    }

    private void findPathInMap(String fileName, SEARCHTYPE searchType) {
        Map initMap = readInMap(fileName);
        switch(searchType) {
            case ASTAR:
                break;
            case BESTFIRST:
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
                    // m.printMap();
                    // System.out.println();
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
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
            for (Map m : tempMaps) {
                if(m != null) {
                    // m.printMap();
                    // System.out.println();
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
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