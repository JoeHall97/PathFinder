import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    public enum SEARCHTYPE {
        DEPTHFIRST,
        BREADTHFIRST,
        BESTFIRST,
        ASTAR
    }

    public static void main(String args[]) {
        PathFinder pathFinder = new PathFinder();
        if(args.length < 2) {
            pathFinder.findPathInMap("../Maps/map.txt", SEARCHTYPE.DEPTHFIRST);
        } else {
            pathFinder.findPathInMap(args[0], SEARCHTYPE.valueOf(args[1].toUpperCase()));
        }
    }

    private void findPathInMap(String fileName, SEARCHTYPE searchType) {
        Map initialMap = readInMap(fileName);
        int[] goalPosition = initialMap.getGoalPosition();
        switch(searchType) {
            case BREADTHFIRST:
                break;
            case BESTFIRST:
                break;
            case ASTAR:
                break;
            case DEPTHFIRST:
            default:
                depthFirstSearch(initialMap,goalPosition);
                break;
        }
    }

    private void depthFirstSearch(Map initialMap, int[] goalPosition) {
        Stack<Map> mapFrontier = new Stack<Map>();
        List<int[]> vistedPositions = new ArrayList<int[]>();
        mapFrontier.add(initialMap);
        vistedPositions.add(initialMap.getCurrentPosition());
        int numExpansions = 0;

        while(mapFrontier.size() > 0) {
            Map temp = mapFrontier.pop();
            ++numExpansions;
            Map[] tempMaps = expandMapState(temp, vistedPositions);
            for (Map m : tempMaps) {
                if(m != null) {
                    // m.printMap();
                    // System.out.println();
                    if(m.getCurrentPosition()[0] == goalPosition[0] && m.getCurrentPosition()[1] == goalPosition[1]) {
                        m.printMap();
                        System.out.println("NUMBER OF EXPANSIONS: " + numExpansions);
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

        if(currentPosition[0]-1 > 0 && !vistedPositions.contains(new int[] {currentPosition[0]-1,currentPosition[1]}) && 
        (currentMap.get(currentPosition[0]-1).charAt(currentPosition[1]) == ' ' ||
            currentMap.get(currentPosition[0]-1).charAt(currentPosition[1]) == 'G')) {
                expandedMaps[0] = new Map(map);
                expandedMaps[0].addToPath(Map.DIRECTION.NORTH);
        }
        if(currentPosition[0]+1 > 0 && !vistedPositions.contains(new int[] {currentPosition[0]+1,currentPosition[1]}) && 
        (currentMap.get(currentPosition[0]+1).charAt(currentPosition[1]) == ' ' ||
            currentMap.get(currentPosition[0]+1).charAt(currentPosition[1]) == 'G')) {
                expandedMaps[1] = new Map(map);
                expandedMaps[1].addToPath(Map.DIRECTION.SOUTH);
        }
        if(currentPosition[1]-1 > 0 && !vistedPositions.contains(new int[] {currentPosition[0],currentPosition[1]-1}) && 
        (currentMap.get(currentPosition[0]).charAt(currentPosition[1]-1) == ' ' ||
            currentMap.get(currentPosition[0]).charAt(currentPosition[1]-1) == 'G')) {
                expandedMaps[2] = new Map(map);
                expandedMaps[2].addToPath(Map.DIRECTION.WEST);
        }
        if(currentPosition[1]+1 > 0 && !vistedPositions.contains(new int[] {currentPosition[0],currentPosition[1]+1}) && 
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