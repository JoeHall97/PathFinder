import java.util.List;
import java.util.ArrayList;

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

    public Map(List<String> map, List<DIRECTION> path) {
        this.mapString = map;
        this.mapPath = path;
        drawPath();
    }

    public Map(Map mapCopy) {
        // need to initialise new list for a deep copy, else manipulating the path effects both map objects
        this.mapString = new ArrayList<String>(mapCopy.getMap());
        this.mapPath = new ArrayList<DIRECTION>(mapCopy.getPath());
        drawPath();
    } 

    public void addToPath(DIRECTION direction) {
        this.mapPath.add(direction);
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

            String line = this.mapString.get(tempPosition[0]);
            line = line.substring(0,tempPosition[1]) + '.' + line.substring(tempPosition[1]+1);
            this.mapString.set(tempPosition[0], line);
        }
        this.currentPosition = tempPosition;
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