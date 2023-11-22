import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public class Map {
    public enum DIRECTION {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private List<String> mapString;
    private List<DIRECTION> mapPath;
    private int[] currentPosition;
    private String currentPositionString;
    private int[] goalPosition;
    private double heuristic; 

    public Map(List<String> map, List<DIRECTION> path, boolean calculateHeuristic) {
        this.mapString = map;
        this.mapPath = path;
        this.goalPosition = getGoalPosition();
        drawPath();
        this.heuristic = calculateHeuristic ? calculateHeuristic() : 0.0d;
    }

    public Map(Map mapCopy) {
        // need to initialise new list for a deep copy, else manipulating the path effects both map objects
        this.mapString = new ArrayList<String>(mapCopy.getMap());
        this.mapPath = new ArrayList<DIRECTION>(mapCopy.getPath());
        this.goalPosition = getGoalPosition();
        drawPath();
        if (mapCopy.getHeuristic() != 0.0d)
            this.heuristic = mapCopy.getHeuristic();
    } 

    public void addToPath(DIRECTION direction) {
        this.mapPath.add(direction);
        drawPath();
        if(heuristic != 0.0d)
            heuristic = calculateHeuristic();
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

            if(this.mapString.get(tempPosition[0]).charAt(tempPosition[1]) != 'G') {
                String line = this.mapString.get(tempPosition[0]);
                line = line.substring(0,tempPosition[1]) + '.' + line.substring(tempPosition[1]+1);
                this.mapString.set(tempPosition[0], line);
            }
        }
        this.currentPosition = tempPosition;
        setCurrentPositionString();
    }

    private double calculateHeuristic()
    {
        int x = (currentPosition[0] - goalPosition[0]) * (currentPosition[0] - goalPosition[0]);
        int y = (currentPosition[1] - goalPosition[1]) * (currentPosition[1] - goalPosition[1]);
        return Math.sqrt(x+y);
    }

    public boolean checkPositionForExpansion(int positionX, int positionY)
    {
        if(positionX < 0 && positionX > mapString.size() && 
        positionY < 0 && positionY > mapString.get(0).length())
            return false;
        return mapString.get(positionX).charAt(positionY) == ' ' ||
            mapString.get(positionX).charAt(positionY) == 'G';
    }

    public void printMap() {
        for (String s : mapString) {
            System.out.println(s);
        }
    }

    public void printPath() {
        System.out.print('[');
        for(DIRECTION d : mapPath) {
            switch(d) {
                case NORTH:
                    System.out.print("North, ");
                    break;
                case SOUTH:
                    System.out.print("South, ");
                    break;
                case EAST:
                    System.out.print("East, ");
                    break;
                case WEST:
                    System.out.print("West, ");
                    break;
                default:
                    break;
            }
        }
        System.out.println(']');
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

    private void setCurrentPositionString()
    {
        this.currentPositionString = this.currentPosition[0] + "," + this.currentPosition[1];
    }

    public int[] getCurrentPosition() {
        return this.currentPosition;
    }

    public String getCurrentPositionString()
    {
        return this.currentPositionString;
    }

    public List<String> getMap() {
        return this.mapString;
    }

    public List<DIRECTION> getPath() {
        return this.mapPath;
    }

    public double getHeuristicAndCost()
    {
        return this.heuristic + this.mapPath.size();
    }

    public double getHeuristic()
    {
        return this.heuristic;
    }
}
