package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import java.io.Serializable;
/**
 * objects of this class represents the position in the path.
 * it holds the current position in the path and a reference to the orderedPath internally.
 * It also holds SimpleIntegerProperties for x and y coordinates, which we have getter methods for,
 *     so we can return them and attach ChangeListers, to decouple the frontend and backend.
 * The SimpleIntegerProperties are updated automatically when we move through the path.
 */
public class PathPosition implements Serializable{

    private int currentPositionInPath;
    private List<Pair<Integer, Integer>> orderedPath;
    private transient SimpleIntegerProperty x;
    private transient SimpleIntegerProperty y;

    /*
     * the integerproperty internal int is the x or y coordinate - since want to use this class to bind with node positions
     * currentPositionInPath is an index of orderedPath
     * usingValue0 is true if using value0 in path, false if using value1
     * orderedPath is the list of path coordinates in pairs
     */
    public PathPosition(int currentPositionInPath, List<Pair<Integer, Integer>> orderedPath){
        this.currentPositionInPath = currentPositionInPath;
        this.orderedPath = orderedPath;
        x = new SimpleIntegerProperty();
        y = new SimpleIntegerProperty();
        // update internal property
        resetCoordinatesBasedOnPositionInPath();
    }

    /**
     * move forward through the path i.e. clockwise
     */
    public void moveDownPath(){
        currentPositionInPath = (currentPositionInPath + 1)%orderedPath.size();
        resetCoordinatesBasedOnPositionInPath();
    }

    /**
     * move backwards through the path, i.e. anticlockwise
     */
    public void moveUpPath(){
        currentPositionInPath = (currentPositionInPath - 1 + orderedPath.size())%orderedPath.size();
        resetCoordinatesBasedOnPositionInPath();
    }

    // public int calculateDownPath(Pair<Integer, Integer> goalPosition){
    //     int count = 0;
    //     int currentIndex = currentPositionInPath;
    //     int goalIndex = orderedPath.indexOf(goalPosition);
    //     while (currentIndex != goalIndex) {
    //         currentIndex = (currentIndex + 1)%orderedPath.size();
    //         count++;
    //     }
    //     return count;
    // }

    // public int calculateUpPath(Pair<Integer, Integer> goalPosition){
    //     int count = 0;
    //     int currentIndex = currentPositionInPath;
    //     int goalIndex = orderedPath.indexOf(goalPosition);
    //     while (currentIndex != goalIndex) {
    //         currentIndex = (currentIndex - 1 + orderedPath.size())%orderedPath.size();
    //         count++;
    //     }
    //     return count;
    // }

    /**
     * change the x and y SimpleIntegerProperties to reflect the current values of
     * the current position in the path, and the ordered path.
     */
    private void resetCoordinatesBasedOnPositionInPath(){
        x.set(orderedPath.get(currentPositionInPath).getValue0());
        y.set(orderedPath.get(currentPositionInPath).getValue1());
    }

    /**
     * Returns x position
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getX(){
        return x;
    }

    /**
     * Returns y position
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getY(){
        return y;
    }
}
