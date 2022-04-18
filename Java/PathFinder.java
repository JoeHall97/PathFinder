import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    public enum DIRECTION {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private class Map {
        private List<String> mapString;
        private List<DIRECTION> mapPath;
        private int[] currentPosition;

        public Map(List<String> map, List<DIRECTION> path) {
            this.mapString = map;
            this.mapPath = path;
            drawPath();
        }

        private void drawPath() {
            if (mapString == null || mapPath == null) {
                System.err.println("ERROR: Map path or string is null");
                return;
            }
            
            int[] tempPosition = getStartPosition();    
            for (DIRECTION d : this.mapPath) {
                switch(d) {
                    case NORTH:
                        tempPosition[0] -= 1;
                        break;
                    case SOUTH:
                        tempPosition[0] += 1;
                        break;
                    case WEST:
                        tempPosition[1] -= 1;
                        break;
                    case EAST:
                        tempPosition[1] += 1;
                        break;
                    default:
                        break;
                }
                System.out.println("TEST");
                String line = this.mapString.get(tempPosition[0]);
                line = line.substring(0,tempPosition[1]) + '.' + line.substring(tempPosition[1]+1);
                this.mapString.set(tempPosition[0], line);
            }
        }

        public void printMap() {
            for (String s : mapString) {
                System.out.println(s);
            }
        }

        public int[] getStartPosition() {
            for(int i=0;i<mapString.size();i++) {
                if(mapString.get(i).contains("S")) {
                    return new int[] {i,mapString.get(i).indexOf("S")};
                }
            }
            return null;
        }

        public int[] getGoalPosition() {
            for(int i=0;i<mapString.size();i++) {
                if(mapString.get(i).contains("G")) {
                    return new int[] {i,mapString.get(i).indexOf("G")};
                }
            }
            return null;
        }

        public int[] getCurrentPosition() {
            return this.currentPosition;
        }

        public List<String> getMap() {
            return this.mapString;
        }

        public List<DIRECTION> getPath() {
            return this.mapPath;
        }
    }

    public static void main(String args[]) {
        PathFinder pathFinder = new PathFinder();
        if(args.length < 1) {
            pathFinder.findPathInMap("../Maps/map.txt");
        } else {
            pathFinder.findPathInMap(args[0]);
        }
    }

    private void findPathInMap(String fileName) {
        Map initialMap = readInMap(fileName);
        int numExpansions = 0;
        int[] goalPosition = initialMap.getGoalPosition();
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
            
            return new Map(mapData,new ArrayList<DIRECTION>());
        } catch(FileNotFoundException fnfe) {
            System.err.println("An error occured reading in the map file.");
            fnfe.printStackTrace();
            return null;
        }
    }
}